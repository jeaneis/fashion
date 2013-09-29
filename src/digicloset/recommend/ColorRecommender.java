package digicloset.recommend;

import digicloset.Props;
import digicloset.clothes.FashionItem;
import edu.stanford.nlp.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import digicloset.colors.*;

/**
 * TODO(gabor) JavaDoc
 *
 * @author Gabor Angeli
 */
public class ColorRecommender extends Recommender {
  private FlickrImages favorites;

  @Override
  public Iterator<Pair<FashionItem, Double>> recommendFrom(FashionItem... input) {
    return Collections.<Pair<FashionItem,Double>>emptyList().iterator();
  }

  @Override
  public double score(FashionItem candidate, FashionItem... input) {
    ArrayList<FashionItem> outfit = new ArrayList();
    outfit.add(candidate);
    for (FashionItem item:input)
    {
        outfit.add(item);
    }
    return ColorRater.RateColors(outfit, favorites);
  }

  public ColorRecommender(String username, int numImages)
  {
     favorites = new FlickrImages(username, numImages);
  }

}
