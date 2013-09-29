package digicloset.colors;
import com.googlecode.flickrjandroid.*;
import com.googlecode.flickrjandroid.favorites.*;
import com.googlecode.flickrjandroid.photos.*;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import javax.imageio.*;
import com.googlecode.flickrjandroid.people.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;


/**
 * Created with IntelliJ IDEA.
 * User: sharon
 * Date: 9/29/13
 * Time: 2:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class FlickrImages {
    private String apiKey;
    private String secret;
    private String token;
    private RequestContext requestContext;
    private FavoritesInterface favorites;
    private PhotosInterface photoInterface;
    private Properties properties;
    private PeopleInterface users;
    private ArrayList<BufferedImage> images;
    private ArrayList<LAB[]> palettes;

    public FlickrImages(String userName, int numImages)
    {
        System.out.println("Loading Flickr favorites from " + userName);
        try
        {
            InputStream in = null;
            try {
                in = new FileInputStream("src/digicloset/colors/resources/setup.properties");
                properties = new Properties();
                properties.load(in);
            } finally {
                in.close();
            }


            apiKey = properties.getProperty("apiKey");
            secret = properties.getProperty("secret");
            token = properties.getProperty("token");

            images = new ArrayList<BufferedImage>();
            palettes = new ArrayList<LAB[]>();

            Flickr flickr = new Flickr(apiKey, secret);
            requestContext = RequestContext.getRequestContext();

            favorites = flickr.getFavoritesInterface();
            photoInterface = flickr.getPhotosInterface();
            users = flickr.getPeopleInterface();

            User user = users.findByUsername(userName);
            //"58435973@N07"
            PhotoList photos = favorites.getPublicList(user.getId(), null, null, numImages,1, null);
            //PhotoList photos = favorites.getList(user, null, null, 0,0, null);

            for (int i=0; i<photos.size(); i++)
            {
                BufferedImage image = ImageIO.read(photoInterface.getImageAsStream(photos.get(i), Size.THUMB));
                images.add(image);
            }
            ComputePalettes();
        }
        catch (Exception e)
        {
            System.out.println("Can't download images");
            e.printStackTrace();
            ComputePalettes();
        }
    }

    private void ComputePalettes()
    {
        for (int i=0; i<images.size(); i++)
        {
            Color[] palette = ColorClustering.KMeans(images.get(i),5,5);
            LAB[] labs = new LAB[palette.length];
            for (int c=0; c<palette.length; c++)
            {
                labs[c] = new LAB(palette[c]);
            }
            palettes.add(labs);
        }
        System.out.println("Computed Palettes");
    }

    public double Score(LAB[] query)
    {
        double score = 0;
        for (int i=0; i<palettes.size(); i++)
        {
            score += ColorUtils.PaletteDist(palettes.get(i), query);
        }
        score /= palettes.size();

        return Math.exp(-score);
    }

    public static void main(String[] args)
    {

        FlickrImages inventory = new FlickrImages("Vic Powles", 5);
        Color[] palette = {new Color(255,0,0), new Color(0,0,0), new Color(0,10,40), new Color(200,120,120), new Color(100, 200, 240)};
        LAB[] labs = ColorUtils.RGBsToLAB(palette);
        System.out.println("Score " + inventory.Score(labs));


    }


}
