package digicloset.colors;
import digicloset.*;

import java.awt.*;

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
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorUtils {

    public static double PaletteDist(LAB[] a, LAB[] b)
    {
        //create the matrix
        assert(a.length == b.length);
        int n = a.length;
        float[][] matrix = new float[n][n];
        for (int i=0; i<n; i++)
        {
            for (int j=0; j<n; j++)
            {
                matrix[i][j] = (float)SqDist(a[i], b[j]);
            }
        }
        HungarianAlgorithm ha = new HungarianAlgorithm();

        double error = 0;
        int[][] assignments = ha.computeAssignments(matrix);

        for (int i=0; i<n; i++)
            error += SqDist(a[assignments[i][0]], b[assignments[i][1]]);
        error /= (n*10000);
        return error;
    }

    public static LAB[] RGBsToLAB(Color[] rgbs)
    {
       LAB[] labs = new LAB[rgbs.length];
       for (int i=0; i<rgbs.length; i++)
           labs[i] = new LAB(rgbs[i]);
       return labs;
    }

    public static double SqDist(Color a, Color b)
    {
        return Math.pow(a.getRed()-b.getRed(),2) + Math.pow(a.getGreen()-b.getGreen(),2) + Math.pow(a.getBlue()-b.getBlue(),2);
    }

    public static double SqDist(double[] a, double[] b)
    {
        assert(a.length == b.length);
        double dist = 0;
        for (int i=0; i<a.length; i++)
        {
            dist += Math.pow(a[i]-b[i],2);
        }
        return dist;
    }

    public static double SqDist(LAB a, LAB b)
    {
        return Math.pow(a.L-b.L,2)+Math.pow(a.A-b.A,2)+Math.pow(a.B-b.B,2);
    }

    public static String ToBase64(BufferedImage image) throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", out);
        byte[] bytes = out.toByteArray();
        String base64 = Base64.encodeToString(bytes,false);
        return base64;
    }

    public static String ColorToHex(Color color)
    {
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        return hex;
    }
}
