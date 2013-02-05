package sf.retriever;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Document {
	Map<Integer, CorefEntity> corefs = null;
	List<Map<String, String>> sentAnnotations = null;

	public Document() {
		corefs = new HashMap<Integer, CorefEntity>();
		sentAnnotations = new ArrayList<Map<String, String>>();
	}

	public void addCorefMention(CorefMention mention) {
		CorefEntity ent = null;
		if (!corefs.containsKey(mention.clustId)) {
			ent = new CorefEntity();
			corefs.put(mention.clustId, ent);
		} else {
			ent = corefs.get(mention.clustId);
		}
		ent.mentions.add(mention);
		if (mention.representative) {
			if (ent.rep!=null && ent.rep.mentionId != mention.mentionId) {
				System.err.println("two reps for the same entity: \nmention = "+mention +"\nent.rep = "+ent.rep);
			}
			ent.rep = mention;
		}

	}
}
