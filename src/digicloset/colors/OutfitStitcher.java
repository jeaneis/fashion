package digicloset.colors;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.*;
import digicloset.clothes.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: sharon
 * Date: 9/28/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */

//Create an outfit image to be rated
public class OutfitStitcher {

    private static BufferedImage RemoveBackground(BufferedImage image)
    {
        //hacky, but just replace pure white with transparency
        ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = Color.WHITE.getRGB() | 0xFF000000;
            public LAB markerLAB = new LAB(100,0,0);

            public final int filterRGB(int x, int y, int rgb) {
                LAB lab = new LAB(new Color(rgb));
                //if ( ( rgb | 0xFF000000 ) == markerRGB ) {
                if (ColorUtils.SqDist(lab, markerLAB) < 6) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                }
                else {
                    // nothing to do
                    return rgb;
                }

            }
        };
        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        result.getGraphics().drawImage(Toolkit.getDefaultToolkit().createImage(ip), 0, 0, null);
        return result;

    }

    public static BufferedImage Stitch(java.util.List<FashionItem> items) throws IOException
    {
        Collections.sort(items);

        //from top to bottom stitch together
        ArrayList<Point> startPoints = new ArrayList<Point>();
        FashionItem prev = null;
        Point prevPoint = new Point(0,0);
        int height = 0;

        for (FashionItem item:items)
        {
            Point startPoint = new Point(prevPoint);
            if (prev != null)
            {
                startPoint.y += prev.getBottomAttachmentPoint().y-item.getTopAttachmentPoint().y;
            }
            startPoints.add(startPoint);
            prev = item;
            prevPoint = startPoint;
            height = prevPoint.y + item.getBottomAttachmentPoint().y;
        }

        int width = items.get(0).getTopAttachmentPoint().x*2;
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = result.createGraphics();
        graphics.setPaintMode();

        for (int i=0; i<items.size(); i++)
        {
            BufferedImage image = ImageIO.read(new File(items.get(i).StandardImage()));
            image = RemoveBackground(image);
            graphics.drawImage(image, startPoints.get(i).x, startPoints.get(i).y, null);
        }


        return result;
    }

    public static void main(String[] args) throws IOException
    {
        System.out.println("Stitching images");
        HashSet<String> topImages = new HashSet<String>();
        //topImages.add("358979_in_pp.jpg");
        topImages.add("384011_in_pp.jpg");

        HashSet<String> bottomImages = new HashSet<String>();
        bottomImages.add("383334_in_pp.jpg");

        Top top = new Top(0, null, null, null, null, null, 0, null, null, null, null, null, null, topImages);
        Bottom bottom = new Bottom(0, null, null, null, null, null, 0, null, null, null, null, null, null, bottomImages);
        ArrayList<FashionItem> items = new ArrayList<FashionItem>();
        items.add(top);
        items.add(bottom);

        BufferedImage image = Stitch(items);
        ImageIO.write(image, "png", new File("stitchedImage.png"));
        System.out.println("Done stitching images");
    }


}
