package digicloset.colors;
import com.flickr4java.flickr.*;
import com.flickr4java.flickr.auth.*;
import com.flickr4java.flickr.favorites.FavoritesInterface;
import com.flickr4java.flickr.photos.*;
import java.awt.image.*;
import java.io.File;
import javax.imageio.*;
import com.flickr4java.flickr.util.IOUtilities;

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
    RequestContext requestContext;
    FavoritesInterface favorites;
    PhotosInterface photoInterface;
    Properties properties;


    public FlickrImages(String user)
    {
        try
        {
            InputStream in = null;
            try {
                in = new FileInputStream("src/digicloset/colors/resources/setup.properties");
                properties = new Properties();
                properties.load(in);
            } finally {
                IOUtilities.close(in);
            }


            apiKey = properties.getProperty("apiKey");
            secret = properties.getProperty("secret");
            token = properties.getProperty("token");

            Flickr flickr = new Flickr(apiKey, secret, new REST());
            requestContext = RequestContext.getRequestContext();
            Auth auth = new Auth();
            auth.setPermission(Permission.READ);
            auth.setToken("token");
            requestContext.setAuth(auth);
            Flickr.debugRequest = false;
            Flickr.debugStream = false;

            favorites = flickr.getFavoritesInterface();
            photoInterface = flickr.getPhotosInterface();

            PhotoList<Photo> photos = favorites.getPublicList(user, 10,1, null);
            for (int i=0; i<photos.size(); i++)
            {
                BufferedImage image = photoInterface.getImage(photos.get(i), 240);
                ImageIO.write(image, "png", new File("flickr"+i+".png"));

            }
        }
        catch (Exception e)
        {
            System.out.println("Can't download images");
            e.printStackTrace();
        }

    }

    public static void main(String[] args)
    {
        FlickrImages images = new FlickrImages("craig.denford");
    }


}
