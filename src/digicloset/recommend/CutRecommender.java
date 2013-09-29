package digicloset.recommend;

import digicloset.clothes.FashionItem;
import edu.stanford.nlp.util.Pair;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: jeaneis
 * Date: 9/29/13
 * Time: 7:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class CutRecommender extends Recommender {
  public CutRecommender(Collection<FashionItem> items)
  {



  }

  @Override
  public Iterator<Pair<FashionItem, Double>> recommendFrom(FashionItem... input) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public double score(FashionItem candidate, FashionItem... input) {
    return 0;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
