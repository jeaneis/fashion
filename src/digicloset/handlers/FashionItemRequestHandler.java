package digicloset.handlers;

import com.google.gson.Gson;
import org.goobs.net.JsonHandler;
import org.goobs.net.WebServer;

import java.util.HashMap;

import digicloset.clothes.FashionItem;

import static edu.stanford.nlp.util.logging.Redwood.Util.*;

/**
 * TODO(gabor) JavaDoc
 *
 * @author Gabor Angeli
 */
public class FashionItemRequestHandler extends JsonHandler {

  @Override
  public String handleJSON(HashMap<String, String> values, WebServer.HttpInfo info) {
    log("RECEIVE item_lookup");
    if (!values.containsKey("id")) { return error("Expected { id: fashion_item_id }"); }
    int id = Integer.parseInt(values.get("id"));
    Gson gson = new Gson();
    log("  id=" + id);
    log("  item=" + FashionItem.idLookup.get(id));
    log("RESPOND item_lookup");
    if (!FashionItem.idLookup.containsKey(id)) { return error("No such item: " + id); }
    return gson.toJson(FashionItem.idLookup.get(id));
  }

}
