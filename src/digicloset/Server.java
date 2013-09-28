package digicloset;

import digicloset.clothes.FashionItem;
import edu.stanford.nlp.util.Function;
import org.goobs.net.WebServer;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static edu.stanford.nlp.util.logging.Redwood.Util.*;

/**
 * The entry class for the server.
 *
 * @author Gabor Angeli
 */
public class Server {

  private static Map<Class, Set<FashionItem>> inventory;

  public static void main(String[] args) {
    Props.exec(new Function<Properties, Object>() {
      @Override
      public Object apply(Properties properties) {
        forceTrack("Reading inventory");
        inventory = FashionItem.read();
        log("" + inventory.keySet().size() + " categories loaded");
        int totalClothes = 0;
        for (Map.Entry<Class, Set<FashionItem>> entry : inventory.entrySet()) {
          totalClothes += entry.getValue().size();
        }
        log("" + totalClothes + " items loaded");
        endTrack("Reading inventory");

        forceTrack("Starting Server");
        WebServer server = new WebServer(Props.SERVER_PORT);
        server.start();
        for (Props.SERVICE service : Props.SERVICE_ENABLE) {
          server.register(service.path, service.handler);
          log("started service " + service);
        }
        endTrack("Starting Server");
        log(GREEN, "waiting for connections...");
        return null;
      }
    }, args);

  }
}
