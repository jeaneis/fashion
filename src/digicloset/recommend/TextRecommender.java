package digicloset.recommend;

import digicloset.clothes.FashionItem;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.Pair;
import org.goobs.sim.DistSim;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static edu.stanford.nlp.util.logging.Redwood.Util.endTrack;
import static edu.stanford.nlp.util.logging.Redwood.Util.forceTrack;
import static edu.stanford.nlp.util.logging.Redwood.Util.log;

/**
 * Created with IntelliJ IDEA.
 * User: jeaneis
 * Date: 9/28/13
 * Time: 8:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextRecommender extends Recommender {

  public final HashMap<FashionItem, Counter<FashionItem>> nearestNeighbors = new HashMap<FashionItem, Counter<FashionItem>>();

  private Collection<FashionItem> items;

  public TextRecommender(Collection<FashionItem> items)
  {
    DistSim model = DistSim.load("model/distsim.ser.gz");
//    model.sim("dog", "cat").get().cos();

    this.items = items;
    forceTrack("Precomputing nearest neighbors");
    // Get vectors
    forceTrack("Computing vectors");
    Map<FashionItem, Set<String>> adjMap = new HashMap<FashionItem, Set<String>>();
    for (FashionItem item : items) {
      adjMap.put(item, item.toAdjectiveSet());
    }
    endTrack("Computing vectors");
    for (FashionItem source : adjMap.keySet()) {
      log("NN for " + source);
      Set<String> sourceAdjs = adjMap.get(source);
      ClassicCounter<FashionItem> neighbors = new ClassicCounter<FashionItem>();

      for (FashionItem cand : adjMap.keySet()) {
        if (source == cand) { continue; }
        Set<String> candAdjs = adjMap.get(cand);

        Set<String> smallSet;
        Set<String> bigSet;
        if (sourceAdjs.size() <= candAdjs.size())
        {
          smallSet = sourceAdjs;
          bigSet = candAdjs;
        }
        else
        {
          smallSet = candAdjs;
          bigSet = sourceAdjs;
        }

//        double[] maxSimVals = new double[smallSet.size()];
        double sumMaxVals = 0;

        for (int i = 0; i < smallSet.size(); i++)
        {
          double maxVal = Double.NEGATIVE_INFINITY;
          String sAdj = smallSet.iterator().next();

          for (String bAdj : bigSet)
          {
            double jaccard = model.sim(sAdj, bAdj).get().jaccard();
            if (jaccard > maxVal)
            {
              maxVal = jaccard;
            }
          }
//          maxSimVals[i] = maxVal;
          sumMaxVals += maxVal;
        }

        double val = 0;
        if (smallSet.size() != 0)
        {
          val = sumMaxVals / smallSet.size();
        }

        neighbors.setCount(cand, val);
      }
      nearestNeighbors.put(source, neighbors);
    }
    endTrack("Precomputing nearest neighbors");
  }

  private void saveNN() {
    try {
      FileWriter fw = new FileWriter("data/vectorNN.tab");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean loadNN() {
    return false;
  }

  private double jaccard(double[] a, double[] b) {
    double numer = 0.0;
    double denom = 0.0;
    for (int i = 0; i < a.length; ++i) {
      numer += (a[i] < b[i] ? a[i] : b[i]);
      denom += (a[i] < b[i] ? b[i] : a[i]);
    }
    return numer / denom;
  }

  /**
   * Compute the average NN similarity between the input items and a target.
   * @param input The input items of clothing.
   * @return An iterator of nearest neightbors.
   */
  @Override
  public Iterator<Pair<FashionItem, Double>> recommendFrom(FashionItem... input) {
    Counter<FashionItem> counts = new ClassicCounter<FashionItem>();
    for (FashionItem in : input) {
      Counters.addInPlace(counts, nearestNeighbors.get(in));
    }
    Counters.divideInPlace(counts, (double) input.length);
    return Counters.toDescendingMagnitudeSortedListWithCounts(counts).iterator();
  }






}
