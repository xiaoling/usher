package el;

import tackbp.KB;

public class StringMatchBaseline {
	public int nilId = 0;
	public int hit = 0;
	public void predict(EntityMention mention, KB kb) {
		if (kb.entityMap.containsKey(mention.mentionString)) {
			mention.entityId = kb.entityMap.get(mention.mentionString).kbId;
			hit++;
		} else {
			mention.entityId = "NIL"+String.format("%04d",nilId++);
		}
	}
}
