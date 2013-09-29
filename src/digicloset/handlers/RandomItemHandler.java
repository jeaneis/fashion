package digicloset.handlers;

import com.google.gson.Gson;
import digicloset.Server;
import digicloset.clothes.FashionItem;
import org.goobs.net.JsonHandler;
import org.goobs.net.WebServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * TODO(gabor) JavaDoc
 *
 * @author Gabor Angeli
 */
public class RandomItemHandler extends JsonHandler {
  @Override
  public String handleJSON(HashMap<String, String> values, WebServer.HttpInfo info) {
    if (!values.containsKey("type")) { return error("no type given. Usage: { type: type_to_random_query }"); }
    for (Class key : Server.inventory.keySet()) {
      if (key.getSimpleName().equalsIgnoreCase(values.get("type"))) {
        List<FashionItem> itemsInType = new ArrayList<FashionItem>(Server.inventory.get(key));
        Random rand = new Random();
        return new Gson().toJson(itemsInType.get(rand.nextInt(itemsInType.size())));
      }
    }
    return error("Could not find type: " + values.get("type"));
  }
}
