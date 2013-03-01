package el;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.apache.commons.lang.StringUtils;

import sf.SFConstants;
import sf.retriever.FakeProcessedCorpus;
import sf.retriever.ProcessedCorpus;
import util.FileUtil;

/**
 * 
 * @author Xiao Ling
 */

public class OpenIeNEL {
	private static Set<String> skippedTypes = null; 
	static {
		skippedTypes = new HashSet<String>();
		skippedTypes.add("DATE");
		skippedTypes.add("DURATION");
		skippedTypes.add("MONEY");
		skippedTypes.add("NUMBER");
		skippedTypes.add("ORDINAL");
		skippedTypes.add("PERCENT");
		skippedTypes.add("SET");
		skippedTypes.add("TIME");
	}

	public static void main(String[] args) {
		el.QueryReader qReader = new el.QueryReader();
		qReader.readFrom(ElConstants.queryFile);
		// StringMatchBaseline baseline = new StringMatchBaseline();
		Set<String> docs = new HashSet<String>();
		for (EntityMention mention : qReader.queryList) {
			docs.add(mention.mentionDoc);
		}
		System.out.println(docs.size());
		ProcessedCorpus corpus;
		String sentId = null;
		try {
			corpus = new FakeProcessedCorpus(docs.toArray(new String[0]),
					SFConstants.dataTypes);

			Map<String, String> annotations = null;
			int c = 0;
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream("data/el.openie"), "UTF8"));
			while (corpus.hasNext()) {
				annotations = corpus.next();
				if (c++ % 100000 == 0) {
					System.err.print("finished reading " + c + " lines\r");
				}

				// check if the file is in the src file set.
				// if not, skip.
				// prerequisite: meta
				sentId = annotations.get(SFConstants.META).split("\t")[0];
				String filename = annotations.get(SFConstants.META).split("\t")[2];
				String tokens = annotations.get(SFConstants.TOKENS).split("\t")[1];
				if (annotations.get(SFConstants.STANFORDNER)
						.split("\t").length == 1) {
					System.out.println("NER:"+annotations.get(SFConstants.STANFORDNER));
					continue;
				}
				if (annotations.get(SFConstants.STANFORDPOS)
						.split("\t").length == 1) {
					System.out.println(annotations.get(SFConstants.STANFORDPOS));
				}
				String postags = annotations.get(SFConstants.STANFORDPOS)
						.split("\t")[1];
				String nerTypes = annotations.get(SFConstants.STANFORDNER)
						.split("\t")[1];
				// verify tokens, postags, ner have the same # of tokens.
				String[] tk = tokens.split(" ");
				{
					String[] pt = postags.split(" ");
					String[] nt = nerTypes.split(" ");
					if (tk.length != pt.length || pt.length != nt.length) {
						System.err.println(sentId + ": errors " + tk.length
								+ " " + pt.length + " " + nt.length);
					}
				}

				ArrayList<String> namedEntitiesTypes = new ArrayList<String>();
				ArrayList<Integer> namedEntitiesStarts = new ArrayList<Integer>();
				ArrayList<Integer> namedEntitiesEnds = new ArrayList<Integer>();
				getNamedEntities(nerTypes, namedEntitiesTypes,
						namedEntitiesStarts, namedEntitiesEnds);
				for (int i = 0; i < namedEntitiesTypes.size(); i++) {
					if (skippedTypes .contains(namedEntitiesTypes.get(i))) {
						continue;
					}
					int argStart = namedEntitiesStarts.get(i);
					int argEnd = namedEntitiesEnds.get(i);
					String arg = StringUtils.join(tk, " ", argStart, argEnd);
					writer.print(String
							.format("%s\treln\tplhd\tX\tX\tX\tX\t%s\t{0}\t{0}\t%s\t%s\tCHUNKS\t%s#%s\tkbp\t1.000\n",
									arg, getBoundString(argStart, argEnd),
									tokens, postags, filename, sentId));
				}
			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("\n"+sentId);
			e.printStackTrace();
		}
	}

	private static void getNamedEntities(String ner,
			ArrayList<String> namedEntitiesTypes,
			ArrayList<Integer> namedEntitiesStarts,
			ArrayList<Integer> namedEntitiesEnds) {
		String[] nerTags = ner.split(" ");
		String prevTag = "O";
		int start = -1;
		for (int i = 0; i < nerTags.length; i++) {
			if (nerTags[i].equals(prevTag)) {

			} else {
				if (prevTag.equals("O")) {
					start = i;
				} else {
					if (nerTags[i].equals("O")) {
						namedEntitiesTypes.add(prevTag);
						namedEntitiesStarts.add(start);
						namedEntitiesEnds.add(i);
						start = -1;
					} else {
						namedEntitiesTypes.add(prevTag);
						namedEntitiesStarts.add(start);
						namedEntitiesEnds.add(i);
						start = i;
					}
				}
			}
			prevTag = nerTags[i];
		}
		if (!prevTag.equals("O")) {
			namedEntitiesTypes.add(prevTag);
			namedEntitiesStarts.add(start);
			namedEntitiesEnds.add(nerTags.length);
		}
	}

	private static Object getBoundString(int argStart, int argEnd) {
		if (argStart != -1) {
			if (argEnd - argStart == 1) {
				return "{" + argStart + "}";
			} else {
				return "[" + argStart + ", " + argEnd + ")";
			}
		}
		return null;
	}
}
