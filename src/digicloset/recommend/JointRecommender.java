package digicloset.recommend;

import digicloset.clothes.FashionItem;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.Pair;

import java.util.Iterator;

/**
 * Combine judgements from multiple fashion recommenders.
 *
 * @author Gabor Angeli
 */
public class JointRecommender extends Recommender {

  private final Recommender[] components;
  private final int buffer = 10;

  public JointRecommender(Recommender... components) {
    this.components = components;
  }

  @Override
  public Iterator<Pair<FashionItem, Double>> recommendFrom(final FashionItem... input) {
    @SuppressWarnings("unchecked") final Iterator<Pair<FashionItem, Double>>[] iters = new Iterator[components.length];
    for (int i = 0; i < components.length; ++i) { iters[i] = components[i].recommendFrom(input); }

    return new Iterator<Pair<FashionItem, Double>>() {
      int componentOnPrix = 0;
      Counter<FashionItem> jointScores = new ClassicCounter<FashionItem>() {{
        for (Iterator<Pair<FashionItem, Double>> iter : iters) {
          for (int k = 0; k < buffer; ++k) {
            if (iter.hasNext()) {
              Pair<FashionItem, Double> x = iter.next();
              setCount(x.first, score(x.first, input));
            }
          }
        }
      }};

      private void enqueue(Pair<FashionItem, Double> elem) {
        jointScores.setCount(elem.first, score(elem.first, input));
      }

      @Override
      public boolean hasNext() {
        return jointScores.size() > 0;
      }

      @Override
      public Pair<FashionItem, Double> next() {
        //noinspection ForLoopReplaceableByForEach
        for (int k = 0; k < components.length; ++k) {
          if (iters[componentOnPrix].hasNext()) {
            enqueue(iters[componentOnPrix].next());
            componentOnPrix += 1;
            componentOnPrix = componentOnPrix % components.length;
            break; }
        }
        FashionItem argmax = Counters.argmax(jointScores);
        double count = jointScores.getCount(argmax);
        jointScores.remove(argmax);
        return Pair.makePair(argmax, count);
      }

      @Override
      public void remove() { throw new AbstractMethodError(); }
    };
  }

  @Override
  public double score(FashionItem candidate, FashionItem... input) {
    double sumScore = 0.0;
    for (Recommender component : this.components) {
      sumScore += component.score(candidate, input);
    }
    return sumScore / ((double) this.components.length);
  }
}
