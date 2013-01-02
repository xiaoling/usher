package el;

import java.util.List;

import tackbp.KB;
import tackbp.RetrieveDocument;

public class MilneNEL {
	public int nilId = 0;
	public int hit = 0;
	public void predict(EntityMention mention, KB kb) {
		List<String> lines = RetrieveDocument.getContent(mention.mentionDoc);
		String sent = null;
		for (String line : lines) {
			int start = -1, end = -1;
			if (line.startsWith(mention.mentionString + " ")) {
				start = 0;
				end = mention.mentionString.length();
			} else if (line.endsWith(" " + mention.mentionString)) {
				start = line.length() - mention.mentionString.length();
				end = line.length();
			} else if (line.contains(" " + mention.mentionString + " ")) {
				start = line.indexOf(" " + mention.mentionString + " ")+1;
				end = start + mention.mentionString.length();
			}
			if (start != -1) {
				sent = line; 
				System.out.println(sent+"\t"+mention.queryId+"\t"+start+"\t"+end);
				break;
			} 
		}
		if (sent == null) {
			System.err.println("sentence not found: "+mention);
		} 

//		if (kb.entityMap.containsKey(mention.mentionString)) {
//			mention.entityId = kb.entityMap.get(mention.mentionString).kbId;
//		} else {
//			mention.entityId = "NIL" + String.format("%04d", nilId++);
//		}
	}
}
