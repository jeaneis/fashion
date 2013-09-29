package digicloset.recommend;

import digicloset.clothes.FashionItem;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static edu.stanford.nlp.util.logging.Redwood.Util.threadAndRun;

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
      private Random rand = new Random();
      Counter<FashionItem> jointScores = new ClassicCounter<FashionItem>() {{
        List<Runnable> queue = new ArrayList<Runnable>();
        for (final Iterator<Pair<FashionItem, Double>> iter : iters) {
          for (int k = 0; k < buffer; ++k) {
            queue.add(new Runnable() {
              @Override
              public void run() {
                if (iter.hasNext()) {
                  Pair<FashionItem, Double> x = iter.next();
                  synchronized (this) { setCount(x.first, score(x.first, input) + rand.nextGaussian() * 0.05); }
                }
              }
            });
          }
        }
        threadAndRun(queue);
      }};

      private void enqueue(Pair<FashionItem, Double> elem) {
        jointScores.setCount(elem.first, score(elem.first, input) + rand.nextGaussian() * 0.05);
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
    boolean usedColor = false;
    for (Recommender component : this.components) {
      if (component instanceof ColorRecommender && new Random().nextInt(5) < 2) {
        sumScore += component.score(candidate, input) * 5;
        usedColor = true;
      } else if ( !(component instanceof ColorRecommender)) {
        sumScore += component.score(candidate, input);

      }
    }
    return sumScore / ((double) (this.components.length + (usedColor ? 4 : -1)));
  }
}
