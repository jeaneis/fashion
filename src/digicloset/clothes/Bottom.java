package digicloset.clothes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.awt.Point;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static edu.stanford.nlp.util.logging.Redwood.Util.println;

/**
 * Pants or skirts
 *
 * @author Gabor Angeli
 */
public class Bottom extends FashionItem {
  public Bottom(int id, String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, double price, String color, String description, Set<String> keywords, String details, Set<Integer> shownWith, Set<Integer> recommended, Set<String> images) {
    super(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
    findBottomAttachment();
  }
}
