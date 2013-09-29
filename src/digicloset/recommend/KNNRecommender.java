package digicloset.recommend;

import digicloset.Props;
import digicloset.clothes.FashionItem;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Counters;
import edu.stanford.nlp.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static edu.stanford.nlp.util.logging.Redwood.Util.*;

/**
 * Returns recommendations based on KNN in vector space.
 *
 * @author Gabor Angeli
 */
public class KNNRecommender extends Recommender {

  public final HashMap<FashionItem, Counter<FashionItem>> nearestNeighbors = new HashMap<FashionItem, Counter<FashionItem>>();

  public KNNRecommender(Collection<FashionItem> items) {
    if (!loadNN(Props.DATA_VECTORNN_FILE, this.nearestNeighbors)) {
      forceTrack("Precomputing nearest neighbors");
      // Get vectors
      forceTrack("Computing vectors");
      Map<FashionItem, double[]> vectors = new HashMap<FashionItem, double[]>();
      for (FashionItem item : items) {
        vectors.put(item, item.toVectorSpace());
      }
      endTrack("Computing vectors");
      for (FashionItem source : vectors.keySet()) {
        log("NN for " + source);
        double[] sourceVector = vectors.get(source);
        ClassicCounter<FashionItem> neighbors = new ClassicCounter<FashionItem>();
        for (FashionItem cand : vectors.keySet()) {
          if (source == cand) { continue; }
          double[] candVector = vectors.get(cand);
          double jaccard = jaccard(sourceVector, candVector);
          neighbors.setCount(cand, jaccard);
        }
        nearestNeighbors.put(source, neighbors);
      }
      saveNN(Props.DATA_VECTORNN_FILE, this.nearestNeighbors);
      endTrack("Precomputing nearest neighbors");
    }
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
   * @return An iterator of nearest neighbors.
   */
  @Override
  public Iterator<Pair<FashionItem, Double>> recommendFrom(FashionItem... input) {
    log("RECEIVE knn lookup");
    Counter<FashionItem> counts = new ClassicCounter<FashionItem>();
    for (FashionItem in : input) {
      Counters.addInPlace(counts, nearestNeighbors.get(in));
    }
    Counters.divideInPlace(counts, (double) input.length);
    log("RESPOND knn lookup");
    return Counters.toDescendingMagnitudeSortedListWithCounts(counts).iterator();
  }
}
