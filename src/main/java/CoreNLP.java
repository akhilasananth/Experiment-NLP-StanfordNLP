import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

import org.apache.log4j.BasicConfigurator;

import edu.stanford.nlp.coref.CorefCoreAnnotations.*;
import edu.stanford.nlp.coref.data.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.trees.TreeCoreAnnotations.*;
import edu.stanford.nlp.util.*;

public class CoreNLP {

  public static void main(String[] args) throws IOException {
      
    BasicConfigurator.configure();
      
    // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
    Properties props = new Properties();
    props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    
    // read some text from the file..
//    File inputFile = new File("/Users/yiwu/Desktop/input.txt");
//    String text = Files.toString(new File("/Users/yiwu/Desktop/input.txt"), Charset.forName("UTF-8")); 
    String text = new String(Files.readAllBytes(Paths.get("/Users/yiwu/Desktop/input.txt")));
    
    // create an empty Annotation just with the given text
    Annotation document = new Annotation(text);

    // run all Annotators on this text
    pipeline.annotate(document);

    // these are all the sentences in this document
    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

    for(CoreMap sentence: sentences) {
      // traversing the words in the current sentence
      // a CoreLabel is a CoreMap with additional token-specific methods
      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
        // this is the text of the token
        String word = token.get(TextAnnotation.class);
        // this is the POS tag of the token
        String pos = token.get(PartOfSpeechAnnotation.class);
        // this is the NER label of the token
        String ne = token.get(NamedEntityTagAnnotation.class);
        
        System.out.println("word: " + word + " pos: " + pos + " ne:" + ne);
      }

      // this is the parse tree of the current sentence
      Tree tree = sentence.get(TreeAnnotation.class);
      System.out.println("parse tree:\n" + tree);

      // this is the Stanford dependency graph of the current sentence
      SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
      System.out.println("dependency graph:\n" + dependencies);
    }

    // This is the coreference link graph
    // Each chain stores a set of mentions that link to each other,
    // along with a method for getting the most representative mention
    // Both sentence and token offsets start at 1!
    Map<Integer, CorefChain> graph = 
        document.get(CorefChainAnnotation.class);
    
  }

}




