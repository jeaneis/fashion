package digicloset.clothes;

import java.util.Set;

/**
 * Pants or skirts
 *
 * @author Gabor Angeli
 */
public class Bottom extends FashionItem {
  protected Bottom(int id, String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, double price, String color, String description, Set<String> keywords, String details, Set<String> shownWith, Set<String> recommended, Set<String> images) {
    super(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
  }
}
