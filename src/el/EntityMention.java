package el;

/**
 * Query mention
 * @author xiaoling
 *
 */
public class EntityMention {
	public String queryId = null;
	public String mentionString = null;
	public String mentionDoc = null;
	public String entityId = null;
	public boolean isValidAnswer() {
		return entityId != null;
	}
	public String toString() {
		if (queryId != null && mentionString!= null && mentionDoc != null) {
			if (entityId == null) {
				return String.format("qid:%s, name:%s, doc:%s, entity:null", queryId , mentionString, mentionDoc);
			} else {
				return String.format("qid:%s, name:%s, doc:%s, entity:%s", queryId , mentionString, mentionDoc, entityId);
			}
		} else {
			return "Uninitialized entity mention.";
		}
		
	}
}
