package digicloset.colors;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.Random;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: sharon
 * Date: 9/28/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */

public class ColorClustering {

    public static class Cluster {
        private int size;
        private double[] sum;
        private LAB label;

        public Cluster(LAB label)
        {
            size = 0;
            sum = new double[]{0,0,0};
            this.label = label;
        }

        public void AddColor(LAB color)
        {
            sum[0] += color.L;
            sum[1] += color.A;
            sum[2] += color.B;
            size++;
        }

        public LAB MeanColor()
        {
            if (size > 0)
                return new LAB((sum[0]/size), (sum[1]/size), (sum[2]/size));
            else
                return label;
        }

        public int getSize()
        {
            return size;
        }

        public void SetLabel(LAB color)
        {
            label = color;
        }

        public void Reset()
        {
            label = MeanColor();
            size = 0;
            sum = new double[]{0,0,0};
        }

        public double SqDist(LAB color)
        {
            //compare color with the label
            return Math.pow(label.L-color.L,2) + Math.pow(label.A-color.A,2)+Math.pow(label.B-color.B,2);
        }
    }


    private static Cluster[] InitSeeds(int k)
    {
        //initialize seeds randomly in color space
        Random random = new Random();
        Cluster[] result = new Cluster[k];

        for (int i=0; i<k; i++)
        {
            int l = (int)Math.round(random.nextDouble()*255);
            int a = (int)Math.round(random.nextDouble()*255);
            int b = (int)Math.round(random.nextDouble()*255);

            Cluster cluster = new Cluster(new LAB(l,a,b));
            result[i] = cluster;
        }
        return result;
    }

    private static LAB[][] ConvertToLAB(BufferedImage image)
    {
        LAB[][] labs = new LAB[image.getWidth()][image.getHeight()];
        for (int i=0; i<image.getWidth(); i++)
        {
            for (int j=0; j<image.getHeight(); j++)
            {
                labs[i][j] = new LAB(new Color(image.getRGB(i,j)));
            }
        }
        return labs;
    }

    private static double KMeansHelper(BufferedImage image, LAB[][] labImage, int k, Cluster[] seeds, Random random)
    {
        int trials = 200;
        int width = image.getWidth();
        int height = image.getHeight();

        int[][] assignments = new int[width][height];


        for (int t=0; t<trials; t++)
        {
            int changes = 0;
            for (int i=0; i<width; i++)
            {
                for (int j=0; j<height; j++)
                {
                    //find closest pixel
                    Color rgba = new Color(image.getRGB(i,j), true);
                    int alpha = rgba.getAlpha();
                    if (alpha == 0)
                        continue;
                    LAB pixel = new LAB(rgba);

                    double bestDist = Double.MAX_VALUE;
                    int bestIndex = 0;

                    for (int s=0; s<seeds.length; s++)
                    {
                        double dist = seeds[s].SqDist(pixel);
                        if (dist < bestDist)
                        {
                            bestDist = dist;
                            bestIndex = s;
                        }
                    }

                    if (assignments[i][j] != bestIndex)
                    {
                        changes++;
                        assignments[i][j] = bestIndex;
                    }

                    seeds[bestIndex].AddColor(pixel);
                }
            } //end image loop

            if (changes == 0)
            {
                break;
            }

            //now re-compute the labels
            if (t < trials-1)
            {
                for (int s=0; s<seeds.length; s++)
                {
                    if (seeds[s].getSize() > 0)
                        seeds[s].Reset();
                    else {        //reseed
                        seeds[s].SetLabel(labImage[(int)(random.nextDouble()*width)][(int)(random.nextDouble()*height)]);
                        //seeds[s].SetLabel(new Color((int)(random.nextDouble()*255), (int)(random.nextDouble()*255), (int)(random.nextDouble()*255)));
                    }
                }
            }

        }

        //compute error
        double error = 0;
        for (int i=0; i<width; i++)
            for (int j=0; j<height; j++)
                error += seeds[assignments[i][j]].SqDist(labImage[i][j]);
        return error;
    }

    public static Color[] KMeans(BufferedImage image, int k, int tries)
    {
        System.out.println("Starting KMeans");

        LAB[][] labImage = ConvertToLAB(image);

        Random random = new Random();
        Color[] result = new Color[k];

        Cluster[] seeds = InitSeeds(k);

        double bestScore = Double.MAX_VALUE;
        for (int i=0; i<tries; i++)
        {
            Cluster[] currSeeds = InitSeeds(k);
            double score = KMeansHelper(image, labImage, k, currSeeds, random);

            if (score < bestScore)
            {
                bestScore = score;
                seeds = currSeeds;
            }
        }

        for (int s=0; s<seeds.length; s++)
        {
            result[s] = seeds[s].MeanColor().toRGB();
        }


        System.out.println("Done with Kmeans");


        return result;
    }

    public static void SaveColors(Color[] colors, String filename) throws IOException
    {
        int colorSize = 100;
        BufferedImage image= new BufferedImage(colorSize*colors.length, colorSize, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image.getGraphics();
        for (int i=0; i<colors.length; i++)
        {
            graphics.setColor(colors[i]);
            graphics.fillRect(i*colorSize, 0, colorSize, colorSize);
        }
        File outputFile = new File(filename);
        ImageIO.write(image, "png", outputFile);
    }

    public static void main(String[] args) throws IOException
    {
        //test Kmeans
        BufferedImage image = ImageIO.read(new File("test.png"));
        Color[] colors = KMeans(image, 5, 3);
        SaveColors(colors, "testPalette.png");
    }

}
