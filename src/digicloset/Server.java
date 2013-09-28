package digicloset;

import edu.stanford.nlp.util.Function;
import org.goobs.net.WebServer;

import java.util.Properties;

import static edu.stanford.nlp.util.logging.Redwood.Util.*;

/**
 * The entry class for the server.
 *
 * @author Gabor Angeli
 */
public class Server {

  public static void main(String[] args) {
    Props.exec(new Function<Properties, Object>() {
      @Override
      public Object apply(Properties properties) {
        forceTrack("Starting Server");
        WebServer server = new WebServer(Props.SERVER_PORT);
        server.start();
        for (Props.SERVICE service : Props.SERVICE_ENABLE) {
          server.register(service.path, service.handler);
          log("started service " + service);
        }
        endTrack("Starting Server");
        return null;
      }
    }, args);

  }
}
