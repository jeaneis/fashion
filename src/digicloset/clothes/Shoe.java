package digicloset.clothes;

import java.util.Set;

/**
 * A shoe / boot / sandal / etc.
 *
 * @author Gabor Angeli
 */
public class Shoe extends FashionItem {
  public Shoe(int id, String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, double price, String color, String description, Set<String> keywords, String details, Set<Integer> shownWith, Set<Integer> recommended, Set<String> images) {
    super(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
  }
}
