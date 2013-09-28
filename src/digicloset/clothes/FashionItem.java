package digicloset.clothes;

import digicloset.Props;
import edu.stanford.nlp.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * An abstract representation of a fashion item
 *
 * @author Gabor Angeli
 */
public abstract class FashionItem {

  public final String metaDescription;
  public final Set<String> metaKeywords;
  public final Set<String> categories;
  public final String brand;
  public final String name;
  public final int price;
  public final String color;
  public final String description;
  public final Set<String> keywords;
  public final String details;

  private final Set<String> shownWith;
  private final Set<String> recommended;
  private final Set<String> images;

  protected FashionItem(String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, int price, String color, String description, Set<String> keywords, String details, Set<String> shownWith, Set<String> recommended, Set<String> images) {
    this.metaDescription = metaDescription;
    this.metaKeywords = metaKeywords;
    this.categories = categories;
    this.brand = brand;
    this.name = name;
    this.price = price;
    this.color = color;
    this.description = description;
    this.keywords = keywords;
    this.details = details;
    this.shownWith = shownWith;
    this.recommended = recommended;
    this.images = images;
  }

  // Measurements


  private static String cleanToken(String input) {
    return input.replaceAll("\\.", "").replaceAll(",", "");
  }

  private static Set<String> cleanTokens(String[] tokens, String... toRemove) {
    Set<String> out = new HashSet<String>();
    for (String tok : tokens) {
      out.add(cleanToken(tok));
    }
    for (String remove : toRemove) {
      out.remove(remove);
    }
    return out;
  }

  @SuppressWarnings("unchecked")
  public static <E extends FashionItem> E createOrNull(String id) {
    try {
      File file = new File(Props.DATA_INFO_DIR.getPath() + File.separator + id + ".html");
      if (!file.exists()) { throw new IllegalStateException("Could not find file: " + file); }

      // Fields to populate
      String metaDescription = "";
      Set<String> metaKeywords = new HashSet<String>();
      Set<String> categories = new HashSet<String>();
      String brand = "";
      String name = "";
      int price = 0;
      String color = "";
      String description = "";
      Set<String> keywords = new HashSet<String>();
      String details = "";
      // Private fields
      Set<String> shownWith = new HashSet<String>();
      Set<String> recommended = new HashSet<String>();
      Set<String> images = new HashSet<String>();

      // Fill general fields
      for (String line : IOUtils.slurpFile(file).split("\n+")) {
        String[] fields = line.split("\\t");
        String key = fields[0];
        if (key.equalsIgnoreCase("meta-description")) { metaDescription = fields[1]; }
        else if (key.equalsIgnoreCase("meta-keywords")) { metaKeywords = cleanTokens(fields[1].split(" ")); }
        else if (key.equalsIgnoreCase("categories")) { categories = cleanTokens(fields, "categories"); }
        else if (key.equalsIgnoreCase("brand")) { brand = fields[1]; }
        else if (key.equalsIgnoreCase("name")) { name = fields[1]; }
        else if (key.equalsIgnoreCase("price")) { price = Integer.parseInt(fields[1].substring(1)); }
        else if (key.equalsIgnoreCase("color")) { color = fields[1]; }
        else if (key.equalsIgnoreCase("description")) { description = fields[1]; }
        else if (key.equalsIgnoreCase("keywords")) { keywords = cleanTokens(fields, "keywords"); }
        else if (key.equalsIgnoreCase("shownWith")) { shownWith = cleanTokens(fields, "shownWith"); }
        else if (key.equalsIgnoreCase("details")) { details = fields[1]; }
        else if (key.equalsIgnoreCase("recommended")) { recommended = cleanTokens(fields, "recommended"); }
        else if (key.equalsIgnoreCase("images")) { images = cleanTokens(fields, "images"); }
      }

      // Categorize
      if (categories.contains("Tops")) {
        return (E) new Top(metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
      } else if (categories.contains("Skirts")) {
        return (E) new Bottom(metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
      } else if (categories.contains("Shoes")) {
        return (E) new Shoe(metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
      } else if (categories.contains("Dresses")) {
        return (E) new Dress(metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
      } else {
        return null;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Map<Class, FashionItem> read() {
    IOUtils.iterFilesRecursive(Props.DATA_INFO_DIR);
    return null;
  }
}
