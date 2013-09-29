package digicloset.handlers;

import com.google.gson.Gson;
import digicloset.clothes.FashionItem;
import digicloset.recommend.Recommender;
import edu.stanford.nlp.util.Pair;
import org.goobs.net.JsonHandler;
import org.goobs.net.WebServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Recommends similar clothes given an input.
 *
 * Input:
 *
 * {
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

  @Override
  public String handleJSON(HashMap<String, String> values, WebServer.HttpInfo info) {
    // Sanity check
    Gson gson = new Gson();
    if (!values.containsKey("input")) { return error("Expected { input: [ item_id* ], start: start_index, count: num_results"); }
    if (!values.containsKey("start")) { return error("Expected { input: [ item_id* ], start: start_index, count: num_results"); }
    if (!values.containsKey("count")) { return error("Expected { input: [ item_id* ], start: start_index, count: num_results"); }
    int[] input = gson.fromJson(values.get("input"), int[].class);
    int start = Integer.parseInt(values.get("start"));
    int count = Integer.parseInt(values.get("count"));

    // Construct nearest neighbors
    FashionItem[] items = new FashionItem[input.length];
    for (int i = 0; i < input.length; ++i) {
      items[i] = FashionItem.idLookup.get(input[i]);
      if (items[i] == null) { error("could not find item: " + items[i]); }
    }
    Iterator<Pair<FashionItem,Double>> iter = recommender.recommendFrom(items);
    for (int i = 0; i < start; ++i) { iter.next(); }

    // Output
    List<HashMap<String,Object>> jsonOut = new ArrayList<HashMap<String,Object>>();
    for (int i = 0; i < count; ++i) {
      Pair<FashionItem, Double> recommendation = iter.next();
      HashMap<String, Object> obj = new HashMap<String, Object>();
      obj.put("id", recommendation.first.id);
      obj.put("score", recommendation.second);
      jsonOut.add(obj);
    }
    return gson.toJson(jsonOut);
  }

}
