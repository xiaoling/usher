package el;

import java.util.HashMap;
import java.util.Map;

import tackbp.KB;
import util.io.FileUtil;

/**
 * 
 * @author Xiao Ling
 */

public class UiucNEL {
	public int nilId = 0;
	public int hit = 0;

	public static void main(String[] args) {
		readOutput();
	}

	public static void readOutput() {
		KB kb = new KB();
		 kb.init();
		el.QueryReader qReader = new el.QueryReader();
		qReader.readFrom(ElConstants.queryFile);
		String outputFolder = "data/el_predictions/uiuc/wikified/";
		// query to metadata
		Map<String, String> q2m = new HashMap<String, String>();
		String[] metadata = FileUtil.getTextFromFile(
				"data/el_predictions/milne.meta").split("\n");
		for (int i = 0; i < metadata.length; i++) {
			String[] fields = metadata[i].split("\t");
			q2m.put(fields[0], metadata[i]);
		}
		StringBuilder results = new StringBuilder();
		int nilId = 0;
		for (int i = 0; i < qReader.queryList.size(); i++) {
			EntityMention query = qReader.queryList.get(i);
			String meta = q2m.get(query.queryId);
			int start = 0, end = query.mentionString.length();
			if (meta != null) {
				String[] fields = meta.split("\t");
				start = Integer.parseInt(fields[1]);
				end = Integer.parseInt(fields[2]);
			}

			String entity = getSegmentWikification(outputFolder + i
					+ ".wikification.tagged.full.xml", start, end);

			if (entity == null) {
				results.append(query.queryId + "\tNIL"
						+ String.format("%04d", nilId++) + "\n");
			} else {
				if (kb.entityMap.containsKey(entity)) {
					results.append(query.queryId + "\t"
							+ kb.entityMap.get(entity).kbId + "\n");
				} else {
					results.append(query.queryId + "\tNIL"
							+ String.format("%04d", nilId++) + "\n");
				}
			}
		}
		FileUtil.writeTextToFile(results.toString(),
				"data/el_predictions/uiuc/el.out");
	}

	public static String getSegmentWikification(String filename, int start,
			int end) {
		String entity = null;
		// factory.setValidating(true);
		try {
			String[] lines = FileUtil.getTextFromFile(filename).split("\n");
			int es = -1, ee = -1;
			boolean hit = false;
			for (String line : lines) {
				if (line.trim().equals("<Entity>")) {
					es = -1;
					ee = -1;
					hit = false;
				}
				if (line.contains("EntityTextStart")) {
					es = Integer.parseInt(getValue(line, "EntityTextStart"));
				}
				if (line.contains("EntityTextEnd")) {
					ee = Integer.parseInt(getValue(line, "EntityTextEnd"));
				}
				if (line.contains("TopDisambiguation") && es == start
						&& ee == end) {
					hit = true;
				}
				if (hit && line.contains("WikiTitle")) {
					entity = getValue(line, "WikiTitle").replace("_", " ");
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("[ERROR]" + filename);
			e.printStackTrace();
		}
		return entity;
	}

	private static String getValue(String line, String tag) {
		int start = line.indexOf(">") + 1;
		int end = line.indexOf("</" + tag + ">");
		return line.substring(start, end);
	}

	public static void prepare() {
		// FIXME : this is using one single sentence for disambiguation
		// SHOULD use the whole document
		el.QueryReader qReader = new el.QueryReader();
		qReader.readFrom(ElConstants.queryFile);
		String inputFolder = "data/el_predictions/uiuc/wikify/";
		// query to sentence
		Map<String, String> q2s = new HashMap<String, String>();
		// query to metadata
		Map<String, String> q2m = new HashMap<String, String>();
		String[] sentences = FileUtil.getTextFromFile(
				"data/el_predictions/milne.input").split("\n");
		String[] metadata = FileUtil.getTextFromFile(
				"data/el_predictions/milne.meta").split("\n");
		for (int i = 0; i < metadata.length; i++) {
			String[] fields = metadata[i].split("\t");
			q2s.put(fields[0], sentences[i]);
			q2m.put(fields[0], metadata[i]);
		}
		for (int i = 0; i < qReader.queryList.size(); i++) {
			EntityMention query = qReader.queryList.get(i);
			String sent = q2s.get(query.queryId);
			if (sent == null) {
				sent = query.mentionString;
				FileUtil.writeTextToFile(sent, inputFolder + i);
			} else {
				FileUtil.writeTextToFile(sent, inputFolder + i);
			}
		}
	}
}
