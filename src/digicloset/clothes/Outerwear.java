package digicloset.clothes;

import java.util.Set;

/**
 * A coat / jacket
 *
 * @author Gabor Angeli
 */
public class Outerwear extends FashionItem {
  public Outerwear(int id, String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, double price, String color, String description, Set<String> keywords, String details, Set<Integer> shownWith, Set<Integer> recommended, Set<String> images) {
    super(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
    findBottomAttachment();
  }

  public Outerwear(int id, String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, double price, String color, String description, Set<String> keywords, String details, Set<Integer> shownWith, Set<Integer> recommended, Set<String> images,
                   double length) {
    super(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images, length);
    findBottomAttachment();
  }

  @Override
  protected int yPos() {
    return 40;
  }
}
