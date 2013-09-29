package digicloset.clothes;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.*;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.process.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: jeaneis
 * Date: 9/28/13
 * Time: 7:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Parser {

  public final StanfordCoreNLP pipeline;

  public Parser(){
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos");
    pipeline = new StanfordCoreNLP(props);
  }

  public Annotation annotateString(String description)
  {
    Annotation ann = new Annotation(description);
    pipeline.annotate(ann);
    return ann;
  }

  public List<String> getAdjectives(String description)
  {
    List<String> adjs = new ArrayList<String>();
    Annotation ann = annotateString(description);
    for (CoreLabel token : ann.get(CoreAnnotations.TokensAnnotation.class))
    {
//      System.out.println(token.tag());
      if (token.tag().startsWith("J"))
      {
        adjs.add(token.tag());
      }
    }
    return adjs;
  }
}
