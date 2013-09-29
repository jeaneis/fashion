package digicloset.recommend;

import digicloset.Props;
import digicloset.clothes.FashionItem;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
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

  public TextRecommender(Collection<FashionItem> items, boolean includeKeywords)
  {
    DistSim model = DistSim.load("models/distsim.ser.gz");
//    model.sim("dog", "cat").get().cos();

    if (!loadNN(Props.DATA_TEXTNN_FILE, this.nearestNeighbors)) {
      forceTrack("Precomputing nearest neighbors");
      // Get vectors
      forceTrack("Computing vectors");
      double weightKeyword = 2;
      double weightAdj = 1;
      double sumWeights = 0;
      Map<FashionItem, Set<String>> adjMap = new HashMap<FashionItem, Set<String>>();
      Map<FashionItem, Set<String>> keywordMap = new HashMap<FashionItem, Set<String>>();
      for (FashionItem item : items) {
        keywordMap.put(item, item.toKeywordSet());
        if (includeKeywords)
        {
          Set<String> allWordSet = item.toAdjectiveSet();
          allWordSet.addAll(item.toKeywordSet());
          adjMap.put(item, allWordSet);
        }
        else
        {
          adjMap.put(item, item.toAdjectiveSet());
        }
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
            String maxWord = "";
            String sAdj = smallSet.iterator().next();
            sAdj = sAdj.toLowerCase().replaceAll("-", " ");

            for (String bAdj : bigSet)
            {
              bAdj = bAdj.toLowerCase().replaceAll("-", " ");
              double jaccard;
              try {
                jaccard = model.sim(sAdj, bAdj).get().jaccard();
              } catch (NoSuchElementException e) {
                jaccard = 0.0;
              }
              if (jaccard > maxVal)
              {
                maxVal = jaccard;
                maxWord = bAdj;
              }
            }

            if (includeKeywords) {
              Set<String> smallKeySet;
              Set<String> bigKeySet;
              if (sourceAdjs.size() <= candAdjs.size())
              {
                smallKeySet = keywordMap.get(source);
                bigKeySet = keywordMap.get(cand);
              }
              else
              {
                smallKeySet = keywordMap.get(cand);
                bigKeySet = keywordMap.get(source);
              }

              if (bigKeySet.contains(maxWord) || smallKeySet.contains(sAdj))
              {
                sumMaxVals += weightKeyword * maxVal;
                sumWeights += weightKeyword;
              }
              else {
                sumMaxVals += weightAdj * maxVal;
                sumWeights += weightAdj;
              }
            }
            else {
              // maxSimVals[i] = maxVal;
              sumMaxVals += maxVal;
            }
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
      saveNN(Props.DATA_TEXTNN_FILE, this.nearestNeighbors);
    }
    endTrack("Precomputing nearest neighbors");
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
