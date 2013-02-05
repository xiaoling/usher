package sf.retriever;

public class CorefMention {

	// 1. docID
	// 2. mention.corefClusterID
	// 3. mention.mentionID
	// 4. global sentenceID?
	// 5. local sentenceID (indexed from 1)
	// 6. mention.startIndex
	// 7. mention.endIndex
	// 8. mention.headIndex
	// 9. mention.position ?
	// 10. mention.mentionSpan
	// 11. mention.mentionType (pron, named, nom)
	// 12. mention.number (plurality)
	// 13. mention.gender
	// 14. mention.animacy
	// 15. if (representative == mention)

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((animacy == null) ? 0 : animacy.hashCode());
		result = prime * result + clustId;
		result = prime * result + endIdx;
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + globalSentId;
		result = prime * result + headIndex;
		result = prime * result + localSentId;
		result = prime * result + mentionId;
		result = prime * result
				+ ((mentionSpan == null) ? 0 : mentionSpan.hashCode());
		result = prime * result
				+ ((mentionType == null) ? 0 : mentionType.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		result = prime * result + (representative ? 1231 : 1237);
		result = prime * result + startIdx;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CorefMention other = (CorefMention) obj;
		if (animacy == null) {
			if (other.animacy != null)
				return false;
		} else if (!animacy.equals(other.animacy))
			return false;
		if (clustId != other.clustId)
			return false;
		if (endIdx != other.endIdx)
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (globalSentId != other.globalSentId)
			return false;
		if (headIndex != other.headIndex)
			return false;
		if (localSentId != other.localSentId)
			return false;
		if (mentionId != other.mentionId)
			return false;
		if (mentionSpan == null) {
			if (other.mentionSpan != null)
				return false;
		} else if (!mentionSpan.equals(other.mentionSpan))
			return false;
		if (mentionType != other.mentionType)
			return false;
		if (number != other.number)
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (representative != other.representative)
			return false;
		if (startIdx != other.startIdx)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CorefMention [clustId=" + clustId + ", mentionId=" + mentionId
				+ ", globalSentId=" + globalSentId + ", localSentId="
				+ localSentId + ", startIdx=" + startIdx + ", endIdx=" + endIdx
				+ ", headIndex=" + headIndex + ", position=" + position
				+ ", mentionSpan=" + mentionSpan + ", mentionType="
				+ mentionType + ", number=" + number + ", gender=" + gender
				+ ", animacy=" + animacy + ", representative=" + representative
				+ "]";
	}

	public int clustId = -1;
	public int mentionId = -1;
	public int globalSentId = -1;
	public int localSentId = -1;
	public int startIdx = -1;
	public int endIdx = -1;
	public int headIndex = -1;
	public String position = null;
	public String mentionSpan = null;
	public MentionType mentionType = null;
	public Plurality number = null;
	public String gender = null;
	public String animacy = null;
	public boolean representative = false;

	enum Plurality {
		SINGULAR, PLURAL
	}

	enum MentionType {
		PRONOMIAL, NAMED, NOMINAL
	}

	public CorefMention(String corefCache) {
		String[] fields = corefCache.split("\t");
		clustId = Integer.parseInt(fields[1]);
		mentionId = Integer.parseInt(fields[2]);
		globalSentId = Integer.parseInt(fields[3]);
		localSentId = Integer.parseInt(fields[4]);
		startIdx = Integer.parseInt(fields[5]);
		endIdx = Integer.parseInt(fields[6]);
		headIndex = Integer.parseInt(fields[7]);
		position = fields[8];
		mentionSpan = fields[9];
		mentionType = MentionType.valueOf(fields[10].toUpperCase());
		number = Plurality.valueOf(fields[11].toUpperCase());
		gender = fields[12];
		animacy = fields[13];
		representative = Boolean.parseBoolean(fields[14]);
	}

}
