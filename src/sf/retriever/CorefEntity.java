package sf.retriever;

import java.util.HashSet;
import java.util.Set;

public class CorefEntity {
	Set<CorefMention> mentions = null;
	CorefMention rep = null;
	public CorefEntity(){
		mentions = new HashSet<CorefMention>();
	}
}
