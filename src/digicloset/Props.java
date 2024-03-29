package digicloset;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import digicloset.handlers.FashionItemRequestHandler;
import digicloset.handlers.PingRequestHandler;
import digicloset.handlers.RandomItemHandler;
import digicloset.recommend.Recommender;
import digicloset.colors.*;
import edu.stanford.nlp.util.Execution;
import edu.stanford.nlp.util.Execution.Option;
import edu.stanford.nlp.util.Function;
import edu.stanford.nlp.util.StringUtils;
import edu.stanford.nlp.util.logging.Redwood;
import org.goobs.net.WebServerHandler;


import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * A collection of properties
 *
 * @author Gabor Angeli
 */
public class Props {
  private static final Redwood.RedwoodChannels logger = Redwood.channels("Exec");

  public static enum SERVICE {
    PING(new PingRequestHandler(), "/ping"),
    RECOMMEND(null, "/recommend"),
    LOOKUP_ITEM(new FashionItemRequestHandler(), "/lookup_item"),
    RANDOM_ITEM(new RandomItemHandler(), "/random_item")
    ;

    public final WebServerHandler handler;
    public final String path;
    SERVICE(WebServerHandler handler, String path) {
      this.handler = handler;
      this.path = path;
    }
  }

  @Option(name="service.enable", gloss="The services to run")
  public static SERVICE[] SERVICE_ENABLE  = new SERVICE[0];

  @Option(name="server.port", gloss="The port to listen to requests from")
  public static int SERVER_PORT  = 8000;//4242;


  @Option(name="data.image.dir", gloss="The directory with the images")
  public static File DATA_IMAGE_DIR = new File("data/images");
  @Option(name="data.info.dir", gloss="The directory with the info files")
  public static File DATA_INFO_DIR  = new File("data/info");
  @Option(name="data.vectornn.file", gloss="The file where the recommended nearest neighbors live")
  public static File DATA_VECTORNN_FILE  = new File("data/vectorNN.tab.gz");
  @Option(name="data.textnn.file", gloss="The file where the text-based nearest neighbors live")
  public static File DATA_TEXTNN_FILE  = new File("data/textNN.tab.gz");
  @Option(name="data.palette.dir", gloss="The directory with the info files")
  public static File DATA_PALETTE_DIR  = new File("data/palette");

  public static int PALETTE_K = 5;

  private static void initializeAndValidate() {
    if (!DATA_INFO_DIR.exists() || !DATA_INFO_DIR.isDirectory()) {
      throw new IllegalStateException("Could not find fashion item info at " + DATA_INFO_DIR);
    }
  }

  public static void exec(final Function<Properties, Object> toRun, String[] args) {
    // Set options classes
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    Execution.optionClasses = new Class<?>[stackTrace.length];
    for (int i=0; i<stackTrace.length; ++i) {
      try {
        Execution.optionClasses[i] = Class.forName(stackTrace[i].getClassName());
      } catch (ClassNotFoundException e) { logger.fatal(e); }
    }
    // Start Program
    if (args.length == 1) {
      // Case: Run with TypeSafe Config file
      if (!new File(args[0]).canRead()) logger.fatal("Cannot read typesafe-config file: " + args[0]);
      Config config = null;
      try {
        config = ConfigFactory.parseFile(new File(args[0]).getCanonicalFile()).resolve();
      } catch (IOException e) {
        System.err.println("Could not find config file: " + args[0]);
        System.exit(1);
      }
      final Properties props = new Properties();
      for (Map.Entry<String, ConfigValue> entry : config.entrySet()) {
        String candidate = entry.getValue().unwrapped().toString();
        if (candidate != null) {
          props.setProperty(entry.getKey(), candidate);
        }
      }
      Execution.exec(new Runnable() { @Override public void run() {
        initializeAndValidate();
        toRun.apply(props);
      } }, props);
    } else {
      // Case: Run with Properties file or command line arguments
      final Properties props = StringUtils.argsToProperties(args);
      Execution.exec(new Runnable() { @Override public void run() {
        Props.initializeAndValidate();
        toRun.apply(props);
      } }, props);
    }
  }
}
