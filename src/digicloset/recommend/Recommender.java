package digicloset.recommend;

import digicloset.clothes.*;
import edu.stanford.nlp.util.Pair;

import java.util.Iterator;

/**
 * The base class for supplying recommendation votes for a given partial set of clothing
 *
 * @author Gabor Angeli
 */
public abstract class Recommender {

  public abstract Iterator<Pair<FashionItem, Double>> recommendFrom(FashionItem... input);

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
}
