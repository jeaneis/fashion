package digicloset.colors;

import digicloset.*;

import java.awt.image.BufferedImage;
import java.awt.image.*;
import digicloset.clothes.*;
import edu.stanford.nlp.util.StringUtils;

import java.awt.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.util.HashSet;
import java.net.*;
import java.io.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created with IntelliJ IDEA.
 * User: sharon
 * Date: 9/28/13
 * Time: 11:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorRater {
    public static String encodeURIComponent(String string) throws Exception{
        return URLEncoder.encode(string, "UTF-8")
                .replaceAll("\\+", "%20")
                .replaceAll("\\%21", "!")
                .replaceAll("\\%27", "'")
                .replaceAll("\\%28", "(")
                .replaceAll("\\%29", ")")
                .replaceAll("\\%7E", "~");

    }

    public static double RateColors(ArrayList<FashionItem> items, FlickrImages favorites)
    {
        double score = -10000;
        String userAgent = "";
        double favoritesScore = 0;

        try
        {
            BufferedImage image = OutfitStitcher.Stitch(items);
            Image resized = image.getScaledInstance(image.getWidth()/4, image.getHeight()/4, Image.SCALE_SMOOTH);
            BufferedImage resizedImage = new BufferedImage(resized.getWidth(null), resized.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            resizedImage.getGraphics().drawImage(resized, 0, 0, null);
            //ImageIO.write(resizedImage, "png", new File("resized.png"));

            Color[] colors = ColorClustering.KMeans(resizedImage, 5, 5);
            //ColorClustering.SaveColors(colors, "outfitPalette.png");
            String[] palette = new String[Props.PALETTE_K];
            for(int i=0; i<colors.length; i++)
                palette[i] = ColorUtils.ColorToHex(colors[i]).replace("#","");


            //palette = new String[]{"F1E5A5","E60D44","FF8C56","FECC5A","7FD5AB"};
            String paletteString = StringUtils.join(palette,"+");
            System.out.println(paletteString);

            String imageData=ColorUtils.ToBase64(resizedImage);

            //String imageData="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";
            //System.out.println(imageData.length());

            imageData = encodeURIComponent(encodeURIComponent(imageData));

            String url = "http://dovahkiin.stanford.edu:8000/score?palette="+paletteString+"&imageData="+imageData;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", userAgent);


            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
            String responseString = response.toString();
            String[] fields = responseString.split(":");
            score = Double.parseDouble(fields[1].replace("}",""));
            favoritesScore = favorites.Score(ColorUtils.RGBsToLAB(colors));
            System.out.println("Favorites score " + favoritesScore);


        } catch (Exception e)
        {
        }

        return score + favoritesScore;

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

        FlickrImages favorites = new FlickrImages("Vic Powles", Props.PALETTE_K);


        System.out.println(RateColors(items, favorites));

    }

}
