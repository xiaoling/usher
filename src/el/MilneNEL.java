package el;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tackbp.KB;
import tackbp.RetrieveDocument;
import util.io.FileUtil;

public class MilneNEL {
	// FIXME 5 mentions are missing
	// these 5 predictions are manually added
	public static void main(String[] args) {
		System.exit(-1);
		KB kb = new KB();
		kb.init();
		String[] lines = FileUtil.getTextFromFile("milne.out").split("\n");
		String[] meta = FileUtil.getTextFromFile("milne.meta").split("\n");
		int id = -3;
		String start = null, end = null, queryId = null;
		String entity = null;
		int nilId = 0;
		for (String line : lines) {
			if (line.equals("All detected topics:")) {
				id++;
				if (id > 0) {
					if (entity == null) {
						System.out.println(queryId + "\tNIL"
								+ String.format("%04d", nilId++));
					} else {
						if (kb.entityMap.containsKey(entity)) {
							System.out.println(queryId + "\t"
									+ kb.entityMap.get(entity).kbId);
						} else {
							System.out.println(queryId + "\tNIL"
									+ String.format("%04d", nilId++));
						}
					}
				}
				if (id >= 0) {
					String[] fields = meta[id].split("\t");
					start = fields[1];
					end = fields[2];
					queryId = fields[0];
					entity = null;
				}
			} else if (id >= 0 && line.startsWith("WIKIFIED")) {
				String[] fields = line.split("\t");
				if (fields[3].equals(start) && fields[4].equals(end)) {
					entity = fields[1];
				}
			}
		}
		if (entity == null) {
			System.out.println(queryId + "\tNIL"
					+ String.format("%04d", nilId++));
		} else {
			System.out.println(queryId + "\t" + entity);
		}
		System.out.println(id);
	}

	public int nilId = 0;
	public int hit = 0;

	public void predict(EntityMention mention, KB kb) {
		List<String> lines = RetrieveDocument.getContent(mention.mentionDoc);
		String sent = null;
		Pattern pattern = Pattern.compile("[\\s\\p{Punct}]"
				+ mention.mentionString + "[\\s\\p{Punct}]");
		for (String line : lines) {
			Matcher m = pattern.matcher(" " + line + " ");
			if (m.find()) {
				// +1-1=0 offset
				int start = m.start(), end = m.end() - 2;
				// if (line.startsWith(mention.mentionString + " ")) {
				// start = 0;
				// end = mention.mentionString.length();
				// } else if (line.endsWith(" " + mention.mentionString)) {
				// start = line.length() - mention.mentionString.length();
				// end = line.length();
				// } else if (line.contains(" " + mention.mentionString + " "))
				// {
				// start = line.indexOf(" " + mention.mentionString + " ") + 1;
				// end = start + mention.mentionString.length();
				// }
				if (start != -1) {
					sent = line;
					System.out.println(sent + "\t" + mention.queryId + "\t"
							+ start + "\t" + end);
					break;
				}
				if (start != -1) {
					sent = line;
					System.out.println(sent + "\t" + mention.queryId + "\t"
							+ start + "\t" + end);
					break;
				} else {
					System.out.println(line + "\tnot found "
							+ mention.mentionString);
				}
			}
		}
		if (sent == null) {
			System.err.println("sentence not found: " + mention);
		}

		// if (kb.entityMap.containsKey(mention.mentionString)) {
		// mention.entityId = kb.entityMap.get(mention.mentionString).kbId;
		// } else {
		// mention.entityId = "NIL" + String.format("%04d", nilId++);
		// }
	}
}
