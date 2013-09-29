package digicloset.clothes;

import java.util.Set;

/**
 * A top
 *
 * @author Gabor Angeli
 */
public class Top extends FashionItem {
  public Top(int id, String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, double price, String color, String description, Set<String> keywords, String details, Set<Integer> shownWith, Set<Integer> recommended, Set<String> images) {
    super(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
    findBottomAttachment();
  }

  @Override
  protected int yPos() {
    return 40;
  }
}
