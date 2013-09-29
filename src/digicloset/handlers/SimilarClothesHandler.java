package digicloset.handlers;

import com.google.gson.Gson;
import digicloset.clothes.FashionItem;
import digicloset.recommend.Recommender;
import edu.stanford.nlp.util.Pair;
import org.goobs.net.JsonHandler;
import org.goobs.net.WebServer;

import java.util.*;

/**
 * Recommends similar clothes given an input.
 *
 * Input:
 *
 * data = {
 *   type: limit_output_clothing_type,
 *   input: [item_id*],
 *   start: start_index,
 *   end: end_index
 * }
 *
 * Output:
 *
 * {
 *    suggestions: [
 *      { id: integer_id, score: a_double_score }
 *    ]
 * }
 *
 * @author Gabor Angeli
 */
public class SimilarClothesHandler extends JsonHandler {

  public final Recommender recommender;

  public SimilarClothesHandler(Recommender recommender) {
    this.recommender = recommender;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String handleJSON(HashMap<String, String> rawRequest, WebServer.HttpInfo info) {
    Gson gson = new Gson();
    HashMap<String, Object> values = gson.fromJson(rawRequest.get("data"), HashMap.class);
    // Sanity check
    if (!values.containsKey("type")) {  return error("Expected { type: type_to_query, input: [ item_id* ], start: start_index, count: num_results"); }
    if (!values.containsKey("input")) { return error("Expected { type: type_to_query, input: [ item_id* ], start: start_index, count: num_results"); }
    if (!values.containsKey("start")) { return error("Expected { type: type_to_query, input: [ item_id* ], start: start_index, count: num_results"); }
    if (!values.containsKey("count")) { return error("Expected { type: type_to_query, input: [ item_id* ], start: start_index, count: num_results"); }
    List<Object> input = (List<Object>) values.get("input");
    int start = (int) Double.parseDouble(values.get("start").toString());
    int count = (int) Double.parseDouble(values.get("count").toString());

    // Construct nearest neighbors
    FashionItem[] items = new FashionItem[input.size()];
    for (int i = 0; i < input.size(); ++i) {
      items[i] = FashionItem.idLookup.get((int) Double.parseDouble(input.get(i).toString()));
      if (items[i] == null) { error("could not find item: " + items[i]); }
    }
    // Switch on the restricting type
    String type = values.get("type").toString();
    Iterator iter;
    if (type.equalsIgnoreCase("all")) {
      iter = recommender.recommendFrom(items);
    } else if (type.equalsIgnoreCase("top")) {
      iter = recommender.recommendTopFrom(items);
    } else if (type.equalsIgnoreCase("bottom")) {
      iter = recommender.recommendBottomFrom(items);
    } else if (type.equalsIgnoreCase("shoe")) {
      iter = recommender.recommendShoeFrom(items);
    } else {
      return error("Invalid clothing type: " + type);
    }
    for (int i = 0; i < start; ++i) { iter.next(); }

    // Output
    List<HashMap<String,Object>> jsonOut = new ArrayList<HashMap<String,Object>>();
    for (int i = 0; i < count; ++i) {
      if (!iter.hasNext()) { break; }
      Pair<FashionItem, Double> recommendation = (Pair<FashionItem, Double>) iter.next();
      HashMap<String, Object> obj = new HashMap<String, Object>();
      obj.put("id", recommendation.first.id);
      obj.put("score", recommendation.second);
      jsonOut.add(obj);
    }
    return gson.toJson(jsonOut);
  }

}
