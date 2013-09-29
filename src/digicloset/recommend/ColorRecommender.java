package digicloset.recommend;

import digicloset.clothes.FashionItem;
import edu.stanford.nlp.util.Pair;

import java.util.Iterator;

/**
 * TODO(gabor) JavaDoc
 *
 * @author Gabor Angeli
 */
public class ColorRecommender extends Recommender {
  @Override
  public Iterator<Pair<FashionItem, Double>> recommendFrom(FashionItem... input) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public double score(FashionItem candidate, FashionItem... input) {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
