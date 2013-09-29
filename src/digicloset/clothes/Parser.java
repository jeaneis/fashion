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

  LexicalizedParser lp = null;

  public Parser(){    lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
    lp.setOptionFlags(new String[]{"-maxLength", "100", "-outputFormat", "penn"});
  }

  public Tree getParseTree(String sentence)
  {
    StringReader inputReader = new StringReader(sentence);

    TokenizerFactory<CoreLabel> tokenizerFactory =
        PTBTokenizer.factory(new CoreLabelTokenFactory(), "americanize=false");
    List<CoreLabel> rawWords2 =
        tokenizerFactory.getTokenizer(inputReader).tokenize();

    Tree parse = lp.apply(rawWords2);

    return parse;
  }

  public Annotation annotateString(String description)
  {
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize,ssplit,pos");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
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
