package digicloset;

import com.google.gson.*;

import org.goobs.net.JsonHandler;
import org.goobs.net.WebServer;
import static edu.stanford.nlp.util.logging.Redwood.Util.*;

import java.util.HashMap;

/**
 * A simple ping service
 *
 * @author Gabor Angeli
 */
public class PingRequestHandler extends JsonHandler {
  @Override
  public String handleJSON(HashMap<String, String> values, WebServer.HttpInfo info) {
    log("RECEIVE PING");
    Gson gson = new Gson();
    log("RESPOND PING");
    return gson.toJson(values);
  }
}
