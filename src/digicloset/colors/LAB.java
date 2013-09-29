package digicloset.colors;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.*;


/**
 * Created with IntelliJ IDEA.
 * User: sharon
 * Date: 9/28/13
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class LAB {
    public double L;
    public double A;
    public double B;

    public Color toRGB()
    {
        double gamma = 2.2;
        double e = 216 / 24389.0;
        double k = 24389 / 27.0;

        double XR = 0.95047;
        double YR = 1.0;
        double ZR = 1.08883;

        double cieL = L;
        double cieA = A;
        double cieB = B;

        double fy = (cieL + 16) / 116.0;
        double  fx = (cieA / 500.0) + fy;
        double fz = fy - cieB / 200.0;

        double[][] M = {{3.2404542, -1.5371385, -0.4985314},
                {-0.9692660, 1.8760108, 0.0415560},
                {0.0556434, -0.2040259, 1.0572252}};
        double xR = Math.pow(fx, 3.0);
        double zR = Math.pow(fz, 3.0);


        xR = (xR > e)? xR : (116 * fx - 16) / k;
        double yR = (cieL > (k * e))? Math.pow((cieL + 16) / 116.0, 3.0) : cieL / k;
        zR = (zR > e)? zR : (116 * fz - 16) / k;

       double x = xR * XR;
       double y = yR * YR;
       double z = zR * ZR;

       double r = M[0][0] * x + M[0][1] * y + M[0][2] * z;
       double g = M[1][0] * x + M[1][1] * y + M[1][2] * z;
       double b = M[2][0] * x + M[2][1] * y + M[2][2] * z;



       double red = Math.pow(clamp(r), 1.0 / gamma);
       double green = Math.pow(clamp(g), 1.0 / gamma);
       double blue = Math.pow(clamp(b), 1.0 / gamma);

       return new Color((int)Math.round(red * 255), (int)Math.round(green * 255), (int)Math.round(blue * 255));
    }

    private double clamp(double value)
    {
        return Math.min(Math.max(value, 0.0), 1.0);
    }

    public LAB(double L, double A, double B)
    {
        this.L = L;
        this.A = A;
        this.B = B;
    }

    public LAB(Color rgb)
    {
        double gamma = 2.2;
        double red = Math.pow(rgb.getRed()/255.0, gamma);
        double green = Math.pow(rgb.getGreen()/255.0, gamma);
        double blue = Math.pow(rgb.getBlue()/255.0, gamma);

        //sRGB to xyz using the D65 illuminant
        //transformation from http://www.brucelindbloom.com
        double[][] M = {{0.4124564, 0.3575761, 0.1804375},
                {0.2126729, 0.7151522, 0.0721750},
                {0.0193339, 0.1191920, 0.9503041}};

        double x = M[0][0] * red + M[0][1] * green + M[0][2] * blue;
        double y = M[1][0] * red + M[1][1] * green + M[1][2] * blue;
        double z = M[2][0] * red + M[2][1] * green + M[2][2] * blue;

        double XR = 0.95047;
        double YR = 1.00000;
        double ZR = 1.08883;

        double e = 216 / 24389.0;
        double k = 24389 / 27.0;

        double xR = x / XR;
        double yR = y / YR;
        double zR = z / ZR;

        double fx = (xR > e)? Math.pow(xR, 1/3.0): (k * xR + 16)/116.0;
        double fy = (yR > e)? Math .pow(yR, 1 / 3.0) : (k * yR + 16) / 116.0;
        double fz = (zR > e)? Math .pow(zR, 1 / 3.0) : (k * zR + 16) / 116.0;

        double cieL = 116 * fy - 16;
        double cieA = 500 * (fx - fy);
        double cieB = 200 * (fy - fz);

        L = cieL;
        A = cieA;
        B = cieB;
    }
}
