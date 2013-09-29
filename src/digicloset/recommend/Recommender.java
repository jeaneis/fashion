package digicloset.recommend;

import digicloset.clothes.*;
import edu.stanford.nlp.stats.ClassicCounter;
import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.util.Pair;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static edu.stanford.nlp.util.logging.Redwood.Util.log;

/**
 * The base class for supplying recommendation votes for a given partial set of clothing
 *
 * @author Gabor Angeli
 */
public abstract class Recommender {

  public abstract Iterator<Pair<FashionItem, Double>> recommendFrom(FashionItem... input);
  public abstract double score(FashionItem candidate, FashionItem... input);

  public Iterator<Pair<Top, Double>> recommendTopFrom(FashionItem... input) { return recommendXFrom(Top.class, input); }
  public Iterator<Pair<Bottom, Double>> recommendBottomFrom(FashionItem... input) { return recommendXFrom(Bottom.class, input); }
  public Iterator<Pair<Dress, Double>> recommendDressFrom(FashionItem... input) { return recommendXFrom(Dress.class, input); }
  public Iterator<Pair<Shoe, Double>> recommendShoeFrom(FashionItem... input) { return recommendXFrom(Shoe.class, input); }

  private <E extends FashionItem> Iterator<Pair<E, Double>> recommendXFrom(final Class<E> clazz, final FashionItem... inputs) {
    for (FashionItem input : inputs) {
      if (clazz.isAssignableFrom(input.getClass())) { throw new IllegalArgumentException("A " + clazz.getSimpleName() + " already exists in your wardrobe: " + input); }
    }
    return new Iterator<Pair<E, Double>>() {
      private Iterator<Pair<FashionItem, Double>> impl = recommendFrom(inputs);
      private Pair<E, Double> nextVal;
      @SuppressWarnings("unchecked")
      @Override
      public boolean hasNext() {
        if (!impl.hasNext()) { return false; }
        Pair<FashionItem, Double> next = impl.next();
        if (clazz.isAssignableFrom(next.first.getClass())) { nextVal =  Pair.makePair((E) next.first, next.second); }
        return nextVal != null || hasNext();
      }
      @Override
      public Pair<E, Double> next() {
        Pair<E, Double> rtn = nextVal;
        nextVal = null;
        return rtn;
      }
      @Override
      public void remove() { throw new AbstractMethodError(); }
    };
  }

  protected void saveNN(File path, Map<FashionItem, Counter<FashionItem>> nearestNeighbors) {
    DecimalFormat df = new DecimalFormat("0.0000");
    try {
      OutputStreamWriter fw = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(path)));
      for (Map.Entry<FashionItem, Counter<FashionItem>> entry : nearestNeighbors.entrySet()) {
        fw.write(entry.getKey().id + "\t");
        for (Map.Entry<FashionItem, Double> countEntry : entry.getValue().entrySet()) {
          fw.write(countEntry.getKey().id + " " + countEntry.getValue() + "\t");
        }
        fw.write("\n");
      }
      fw.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected boolean loadNN(File path, Map<FashionItem, Counter<FashionItem>> nearestNeighbors) {
    try {
      BufferedReader is = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(path))));
      String line;
      while ( (line = is.readLine()) != null ) {
        String[] fields = line.trim().split("\t");
        FashionItem source = FashionItem.idLookup.get(Integer.parseInt(fields[0]));
        Counter<FashionItem> counts = new ClassicCounter<FashionItem>();
        for (int i = 1; i < fields.length; ++i) {
          String[] targetAndScore = fields[i].split(" ");
          counts.incrementCount(FashionItem.idLookup.get(Integer.parseInt(targetAndScore[0])),
              Double.parseDouble(targetAndScore[1]));
        }
        nearestNeighbors.put(source, counts);
      }
      log("read cached NN file");
      return true;
    } catch (IOException e) {
      if (! (e instanceof FileNotFoundException)) { e.printStackTrace(); }
      return false;
    }
  }
}
