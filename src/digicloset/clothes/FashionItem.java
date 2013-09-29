package digicloset.clothes;

import digicloset.Props;
import digicloset.colors.*;

import edu.stanford.nlp.io.IOUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.HashIndex;
import edu.stanford.nlp.util.Index;

import static edu.stanford.nlp.util.logging.Redwood.Util.*;

/**
 * An abstract representation of a fashion item
 *
 * @author Gabor Angeli
 */
public abstract class FashionItem implements Comparable<FashionItem> {

  public static final Map<Integer, FashionItem> idLookup = new HashMap<Integer, FashionItem>();
  private static final Index<String> featurizer = new HashIndex<String>();
  private static boolean featurizerPopulated = false;
  private static final StanfordCoreNLP pipeline;

  static {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos");
    pipeline = new StanfordCoreNLP(props);
  }

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

  public final double length;

  public final Set<Integer> shownWith;
  public final Set<Integer> recommended;
  public final Set<String> images;

  private Point topAttachmentPoint;
  private Point bottomAttachmentPoint;
  private static int defaultHeight=585;
  private static int defaultWidth=390;

  public FashionItem(int id, String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, double price, String color, String description, Set<String> keywords, String details, Set<Integer> shownWith, Set<Integer> recommended, Set<String> images) {
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

    this.length = 0.0;

    try
    {
      BufferedImage image = ImageIO.read(new File(StandardImage()));
      this.bottomAttachmentPoint = new Point(image.getWidth()/2, image.getHeight());
      this.topAttachmentPoint = new Point(image.getWidth()/2, 0);

    } catch (IOException e)
    {
        println("Error reading image");
    }

  }

  public FashionItem(int id, String metaDescription, Set<String> metaKeywords, Set<String> categories, String brand, String name, double price, String color, String description, Set<String> keywords, String details, Set<Integer> shownWith, Set<Integer> recommended, Set<String> images,
                     double length) {
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

    this.length = length;

    try
    {
      BufferedImage image = ImageIO.read(new File(StandardImage()));
      this.bottomAttachmentPoint = new Point(image.getWidth()/2, image.getHeight());
      this.topAttachmentPoint = new Point(image.getWidth()/2, 0);

    } catch (IOException e)
    {
      println("Error reading image");
    }

  }

  public Point getTopAttachmentPoint()
  {
      return this.topAttachmentPoint;
  }

  public Point getBottomAttachmentPoint()
  {
      return this.bottomAttachmentPoint;
  }

  protected void findTopAttachment()
  {
      try
      {
          BufferedImage image = ImageIO.read(new File(StandardImage()));
          int x = image.getWidth()/2;
          LAB white = new LAB(100,0,0);
          for (int i=0; i<image.getHeight(); i++)
          {
              LAB lab = new LAB(new Color(image.getRGB(x, i)));
              if (ColorUtils.SqDist(lab, white) > 5)
              {
                this.topAttachmentPoint = new Point(x,i);
                break;
              }
          }
      } catch (IOException e)
      {
          this.topAttachmentPoint = new Point(defaultWidth/2, 0);
          println("Error reading image");
      }
  }

  protected void findBottomAttachment()
  {
      try
      {
          BufferedImage image = ImageIO.read(new File(StandardImage()));
          LAB white = new LAB(100,0,0);
          int x = image.getWidth()/2;
          for (int i=image.getHeight()-1; i>=0; i--)
          {
              LAB lab = new LAB(new Color(image.getRGB(x, i)));
              if (ColorUtils.SqDist(lab, white) > 5)
              {
                  this.bottomAttachmentPoint = new Point(x,i);
                  break;
              }
          }
      } catch (IOException e)
      {
          this.bottomAttachmentPoint = new Point(defaultWidth/2, defaultHeight-1);
          println("Error reading image "+StandardImage());
      }
  }

  public String StandardImage()
  {
      //find the image containing _in_
      for (String image:images)
      {
         if (image.contains("_in_pp"))
             return Props.DATA_IMAGE_DIR.getPath() + File.separator + image;
      }
      String standard = Props.DATA_IMAGE_DIR.getPath() + File.separator + images.iterator().next();
      println("No Standard image " + standard);
      return  standard;
  }

  protected abstract int yPos();

  private double[] relatedIndicator(int depth, double[] feats, double factor, Set<Integer> seen) {
    if (depth == 0 || seen.contains(this.id)) {
        return feats;
    } else {
      seen.add(this.id);
      for (int cooc : shownWith) {
        feats[featurizer.indexOf("shownWith-" + cooc)] += factor;
        idLookup.get(cooc).relatedIndicator(depth - 1, feats, factor / 2, seen);
      }
      for (int rec : recommended) {
        feats[featurizer.indexOf("recommended-" + rec)] += factor;
        idLookup.get(rec).relatedIndicator(depth - 1, feats, factor / 2, seen);
      }
      return feats;
    }
  }
  private static List<String> tops = Arrays.asList("Knitwear", "Tops");
  private static List<String> bottoms = Arrays.asList("Pants", "Jeans", "Shorts", "Skirts");
  private static List<String> dresses = Arrays.asList("Jumpsuits", "Dresses");
  private static List<String> outerwear = Arrays.asList("Coats", "Jackets");

  public Set<String> toAdjectiveSet()
  {
    return new HashSet<String>(getAdjectives(description));
  }

  private Annotation annotateString(String description)
  {
    Annotation ann = new Annotation(description);
    pipeline.annotate(ann);
    return ann;
  }

  private List<String> getAdjectives(String description)
  {
    List<String> adjs = new ArrayList<String>();
    Annotation ann = annotateString(description);
    for (CoreLabel token : ann.get(CoreAnnotations.TokensAnnotation.class))
    {
//      System.out.println(token.tag());
      if (token.tag().startsWith("JJ"))
      {
        adjs.add(token.tag().toLowerCase().replaceAll("-", ""));
      }
    }
    return adjs;
  }

  public Set<String> toKeywordSet()
  {
    Set<String> keySet = new HashSet<String>();
    for (String s : keywords)
    {
      keySet.add(s.toLowerCase().replaceAll("-", ""));
    }
    return keySet;
  }

  public double[] toVectorSpace() {
    // Ensure indexer is populated
    if (!featurizerPopulated) {
      startTrack("Populating indexer");
      for (int id : idLookup.keySet()) {
        featurizer.indexOf("shownWith-" + id, true);
        featurizer.indexOf("recommended-" + id, true);
      }
      featurizerPopulated = true;
      endTrack("Populating indexer");
    }
    // Create features
    double[] related = relatedIndicator(5, new double[featurizer.size()], 1.0, new HashSet<Integer>());
    return related;
  }

  @Override
  public int compareTo(FashionItem other) {
    return other.yPos() - this.yPos();
  }

  private static String cleanToken(String input) {
    return input.replaceAll("\\.", " ").replaceAll(",", " ").replaceAll("(\n|\r)+", "").replaceAll("\\s+", " ").trim();
  }

  private static String cleanParagraph(String input) {
    return input.replaceAll("(\n|\r)+", "").replaceAll("\\s+", " ").trim();
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

  private static Set<String> getPathTokens(String[] tokens, String... toRemove) {
      Set<String> out = new HashSet<String>();
      for (String tok:tokens) {
          Boolean exists = new File(Props.DATA_IMAGE_DIR + File.separator + tok).exists();
          if (exists)
            out.add(tok);
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

      double length = 0.0;
      int lengthIndex = -1;

      // Fill general fields
      for (String line : IOUtils.slurpFile(file).split("\n+")) {
        String[] fields = line.split("\\t");
        String key = fields[0];
        if (key.equalsIgnoreCase("meta-description")) { metaDescription = cleanToken(fields[1]); }
        else if (key.equalsIgnoreCase("meta-keywords")) { metaKeywords = cleanTokens(fields[1].split(" ")); }
        else if (key.equalsIgnoreCase("categories")) { categories = cleanTokens(fields, "categories"); }
        else if (key.equalsIgnoreCase("brand")) { brand = cleanToken(fields[1]); }
        else if (key.equalsIgnoreCase("name")) { name = cleanToken(fields[1]); }
        else if (key.equalsIgnoreCase("price")) {
          price = Double.parseDouble(fields[1].substring(1).replaceAll(",", ""));
        }
        else if (key.equalsIgnoreCase("color")) { color = cleanToken(fields[1]); }
        else if (key.equalsIgnoreCase("description")) { description = cleanParagraph(fields[1]); }
        else if (key.equalsIgnoreCase("keywords")) { keywords = cleanTokens(fields, "keywords"); }
        else if (key.equalsIgnoreCase("shownWith")) { shownWith = cleanTokensInt(fields, "shownWith"); }
        else if (key.equalsIgnoreCase("details")) { details = cleanToken(fields[1]); }
        else if (key.equalsIgnoreCase("recommended")) { recommended = cleanTokensInt(fields, "recommended"); }
        else if (key.equalsIgnoreCase("images")) { images = getPathTokens(fields, "images"); }
        else if (key.equalsIgnoreCase("measurements.header")) {
          for (int i = 1; i < fields.length; i++)
          {
            String field = fields[i];
            if (field.equalsIgnoreCase("Length"))
            {
              lengthIndex = i;
              break;
            }
          }
        }
        else if (key.equalsIgnoreCase("measurements.row.1")) {
          if (lengthIndex != -1)
          {
            try {
              length = Double.parseDouble(fields[lengthIndex]);
            }
            catch (Exception e) { }
          }
        }
      }

      if (images.size() == 0)
          return null;

      // Categorize
      if (categories.contains("Clothing"))
      {
        if (isMember(categories, tops)) {
          if (length > 0)
          {
            return (E) new Top(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images, length);
          }
          return (E) new Top(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
        } else if (isMember(categories, bottoms)) {
          if (length > 0)
          {
            return (E) new Top(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images, length);
          }
          return (E) new Bottom(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
        } else if (isMember(categories, dresses)) {
          if (length > 0)
          {
            return (E) new Top(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images, length);
          }
          return (E) new Dress(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
        } else if (isMember(categories, outerwear)) {
          if (length > 0)
          {
            return (E) new Top(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images, length);
          }
          return (E) new Outerwear(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
        }
        else {
          return null;
        }
      } else if (categories.contains("Shoes")) {
        return (E) new Shoe(id, metaDescription, metaKeywords, categories, brand, name, price, color, description, keywords, details, shownWith, recommended, images);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof FashionItem)) return false;
    FashionItem that = (FashionItem) o;
    return id == that.id;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public String toString() {
    return "FashionItem{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }

  public static boolean isMember(Set<String> categories, List<String> list) {
    for(String item : list)
    {
      if (categories.contains(item))
      {
        return true;
      }
    }
    return false;
  }
}
