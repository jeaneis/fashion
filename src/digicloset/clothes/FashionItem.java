package digicloset.clothes;

import digicloset.Props;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static edu.stanford.nlp.util.logging.Redwood.Util.*;

/**
 * An abstract representation of a fashion item
 *
 * @author Gabor Angeli
 */
public abstract class FashionItem {

  public static final Map<Integer, FashionItem> idLookup = new HashMap<Integer, FashionItem>();

  public final int id;
  public final String metaDescription;
  public final Set<String> metaKeywords;
  public final Set<String> categories;
  public final String brand;
  public final String name;
  public final double price;
  public final String color;
  public final String description;
  public final Set<String> keywords;
  public final String details;

  public final Set<Integer> shownWith;
  public final Set<Integer> recommended;
  public final Set<String> images;


  protected FashionItem(int id, String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, double price, String color, String description, Set<String> keywords, String details, Set<Integer> shownWith, Set<Integer> recommended, Set<String> images) {
    this.id = id;
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
    return input.replaceAll("\\.", "").replaceAll(",", "").replaceAll("\n+", "").replaceAll("\\s+", " ").trim();
  }

  private static Set<String> cleanTokens(String[] tokens, String... toRemove) {
    Set<String> out = new HashSet<String>();
    for (String tok : tokens) {
      String clean = cleanToken(tok);
      if (!clean.equals("")) { out.add(clean); }
    }
    for (String remove : toRemove) {
      out.remove(remove);
    }
    return out;
  }

  private static Set<Integer> cleanTokensInt(String[] tokens, String... toRemove) {
    Set<Integer> out = new HashSet<Integer>();
    Set<String> strings = cleanTokens(tokens, toRemove);
    for (String str : strings) { out.add(Integer.parseInt(str)); }
    return out;
  }

  @SuppressWarnings("unchecked")
  public static <E extends FashionItem> E createOrNull(String path) {
    try {
      File file = new File(path);
      if (!file.exists()) { file = new File(Props.DATA_INFO_DIR.getPath() + File.separator + path); }
      if (!file.exists()) { file = new File(Props.DATA_INFO_DIR.getPath() + File.separator + path + ".html"); }
      if (!file.exists()) { throw new IllegalStateException("Could not find file: " + file); }
      int id = Integer.parseInt(path.substring(Math.max(0, path.lastIndexOf(File.separator) + 1), path.lastIndexOf(".") > 0 ? path.lastIndexOf(".") : path.length()));

      // Fields to populate
      String metaDescription = "";
      Set<String> metaKeywords = new HashSet<String>();
      Set<String> categories = new HashSet<String>();
      String brand = "";
      String name = "";
      double price = 0.0;
      String color = "";
      String description = "";
      Set<String> keywords = new HashSet<String>();
      String details = "";
      // Private fields
      Set<Integer> shownWith = new HashSet<Integer>();
      Set<Integer> recommended = new HashSet<Integer>();
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
        else if (key.equalsIgnoreCase("price")) {
          price = Double.parseDouble(fields[1].substring(1).replaceAll(",", ""));
        }
        else if (key.equalsIgnoreCase("color")) { color = fields[1]; }
        else if (key.equalsIgnoreCase("description")) { description = fields[1]; }
        else if (key.equalsIgnoreCase("keywords")) { keywords = cleanTokens(fields, "keywords"); }
        else if (key.equalsIgnoreCase("shownWith")) { shownWith = cleanTokensInt(fields, "shownWith"); }
        else if (key.equalsIgnoreCase("details")) { details = fields[1]; }
        else if (key.equalsIgnoreCase("recommended")) { recommended = cleanTokensInt(fields, "recommended"); }
        else if (key.equalsIgnoreCase("images")) { images = cleanTokens(fields, "images"); }
      }

      // Categorize
      if (categories.contains("Tops")) {
        return (E) new Top(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
      } else if (categories.contains("Skirts") || categories.contains("Pants") || categories.contains("Jeans")) {
        return (E) new Bottom(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
      } else if (categories.contains("Shoes")) {
        return (E) new Shoe(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
      } else if (categories.contains("Dresses")) {
        return (E) new Dress(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
      } else {
//        debug("unknown categories: " + StringUtils.join(categories, " ").replaceAll("\\s+", " "));
        return null;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static Map<Class, Set<FashionItem>> read() {
    // Read Items
    forceTrack("Reading Files");
    HashMap<Class, Set<FashionItem>> items = new HashMap<Class, Set<FashionItem>>();
    for (File file : IOUtils.iterFilesRecursive(Props.DATA_INFO_DIR)) {
      FashionItem item = createOrNull(file.getPath());
      if (item != null) {
        if (!items.containsKey(item.getClass())) { items.put(item.getClass(), new HashSet<FashionItem>()); }
        items.get(item.getClass()).add(item);
        idLookup.put(item.id, item);
      }
    }
    endTrack("Reading Files");
    // Symmeterize Items
    forceTrack("Symeterizing Items");
    for (FashionItem item : idLookup.values()) {
      Iterator<Integer> recommendedIter = item.recommended.iterator();
      while (recommendedIter.hasNext()) {
        int related = recommendedIter.next();
        if (idLookup.containsKey(related)) { idLookup.get(related).recommended.add(item.id); } else { recommendedIter.remove(); }
      }
      Iterator<Integer> shownWithIter = item.shownWith.iterator();
      while(shownWithIter.hasNext()) {
        int shownWith = shownWithIter.next();
        if (idLookup.containsKey(shownWith)) { idLookup.get(shownWith).shownWith.add(item.id); } else { shownWithIter.remove(); }
      }
    }
    endTrack("Symeterizing Items");
    return items;
  }
}
