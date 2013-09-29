package digicloset.clothes;

import java.util.Set;

/**
 * A top
 *
 * @author Gabor Angeli
 */
public class Top extends FashionItem {
  protected Top(String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, int price, String color, String description, Set<String> keywords, String details, Set<String> shownWith, Set<String> recommended, Set<String> images) {
    super(metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
  }
}
