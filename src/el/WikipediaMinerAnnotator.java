package el;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;

import org.wikipedia.miner.annotation.Disambiguator;
import org.wikipedia.miner.annotation.Topic;
import org.wikipedia.miner.annotation.TopicDetector;
import org.wikipedia.miner.annotation.preprocessing.DocumentPreprocessor;
import org.wikipedia.miner.annotation.preprocessing.PreprocessedDocument;
import org.wikipedia.miner.annotation.preprocessing.WikiPreprocessor;
import org.wikipedia.miner.annotation.tagging.DocumentTagger;
import org.wikipedia.miner.annotation.tagging.WikiTagger;
import org.wikipedia.miner.annotation.weighting.LinkDetector;
import org.wikipedia.miner.model.Wikipedia;
import org.wikipedia.miner.util.Position;
import org.wikipedia.miner.util.WikipediaConfiguration;

public class WikipediaMinerAnnotator {
	DocumentPreprocessor _preprocessor;
	Disambiguator _disambiguator;
	TopicDetector _topicDetector2;
	LinkDetector _linkDetector;
	DocumentTagger _tagger;

	DecimalFormat _df = new DecimalFormat("#0%");

	public WikipediaMinerAnnotator(Wikipedia wikipedia) throws Exception {
		// TODO
		_linkDetector = new LinkDetector(wikipedia);

		_preprocessor = new WikiPreprocessor(wikipedia);
		_disambiguator = new Disambiguator(wikipedia);
		_topicDetector2 = new TopicDetector(wikipedia, _disambiguator, true,
				false);
		_tagger = new WikiTagger();

//		String text = "Mixed martial artists competing in Pride parade around the ring during the tournament's opening ceremony";
//		annotate(text);
	}

	public String annotate(String originalMarkup) throws Exception {
		String[] pair = originalMarkup.split("\t");
		if (pair.length!=2) {
			System.out.println(originalMarkup);
		}
		// if (pair.length==1){
		PreprocessedDocument doc = _preprocessor.preprocess(pair[1]);
		StringBuilder output = new StringBuilder(pair[0]);

		// prepare char offsets
		String[] tokens = pair[1].split(" ");
		int[] offsets = new int[tokens.length + 1];
		offsets[0] = 0;
		for (int i = 1; i < tokens.length + 1; i++) {
			offsets[i] = offsets[i - 1] + tokens[i - 1].length() + 1;
		}

		// wikification
		Collection<Topic> allTopics = _topicDetector2.getTopics(doc, null);
		// System.out.println("\nAll detected topics:");
		HashMap<Integer, Double> weights = _linkDetector
				.getTopicWeights(allTopics);
		for (Topic t : allTopics) {
			for (Position pos : t.getPositions()) {
				// change char offset to word offset
				int tokenStart = -1, tokenEnd = -1;
				for (int i = 0; i < offsets.length - 1; i++) {
					if (pos.getStart() >= offsets[i] - 1
							&& pos.getStart() < offsets[i + 1] - 1) {
						tokenStart = i;
					}
					if (pos.getEnd() > offsets[i]
							&& pos.getEnd() <= offsets[i + 1]) {
						tokenEnd = i + 1;
					}
				}
				if (tokenStart < tokenEnd) {
					output.append("\t" + tokenStart + " " + tokenEnd + " "
							+ t.getTitle().replace(" ", "_") + " "
							+ weights.get(t.getId()));
				} else {
					System.err.println("sent " + originalMarkup + " ::: token:"
							+ tokenStart + ", " + tokenEnd + " char:"
							+ pos.getStart() + ", " + pos.getEnd());
				}
			}
		}

		// ArrayList<Topic> bestTopics = _linkDetector.getBestTopics(allTopics,
		// 0.5);
		// System.out.println("\nTopics that are probably good links:");
		// for (Topic t:bestTopics)
		// System.out.println(" - " + t.getTitle() + "[" +
		// _df.format(t.getWeight()) + "]" ) ;
		// String newMarkup = _tagger.tag(doc, bestTopics, RepeatMode.ALL);
		// System.out.println("\nAugmented markup:\n" + newMarkup + "\n") ;
		return output.toString();
	}

	/**
	 * http://wikipedia-miner.cms.waikato.ac.nz/wiki/Wiki.jsp?page=A%20command-
	 * line%20document%20annotator
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String args[]) throws Exception {
		args = new String[] {
				"/projects/pardosa/data12/xiaoling/wikipediaminer/my-wikipedia-template.xml",
				"/projects/pardosa/s5/TACKBP/data/09nw/sentences.tokens",
				"/projects/pardosa/s5/TACKBP/data/09nw/sentences.wikification.milne", };
		WikipediaConfiguration conf = new WikipediaConfiguration(new File(
				args[0]));
//		conf.clearDatabasesToCache();
//		conf.addDatabaseToCache(DatabaseType.label) ;
//        conf.addDatabaseToCache(DatabaseType.pageLinksInNoSentences) ;
        
		Wikipedia wikipedia = new Wikipedia(conf, true);
		// PreprocessedDocument doc = _preprocessor.preprocess(originalMarkup) ;
		WikipediaMinerAnnotator annotator = new WikipediaMinerAnnotator(
				wikipedia);
		// String text =
		// "Mixed martial artists competing in Pride parade around the ring during the tournament's opening ceremony";
		// System.out.println(annotator.annotate(text));
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(args[1]), "UTF8"));
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream(args[2]), "UTF8"));
		int i = 0;
		while (true) {
			if (i++ % 1000 == 0) {
				System.out.print("finished "+i+"\r");
			}
			// System.out.println("Enter snippet to annotate (or ENTER to quit):");
			String line = reader.readLine();

			if (line == null || line.trim().length() == 0)
				break;

			writer.println(annotator.annotate(line));
		}
		writer.close();
		reader.close();
	}
}
