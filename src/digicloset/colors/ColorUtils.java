package digicloset.colors;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: sharon
 * Date: 9/28/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorUtils {
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
}
