package sf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tackbp.KbEntity;
import el.EntityMention;

public class SFEntityMention extends EntityMention {
	public KbEntity.EntityType entityType = null;
	public List<String> ignoredSlots = new ArrayList<String>();
	public Map<String, Answer> answers = new HashMap<String, Answer>();
	public static class Answer {
		
	}
	public static class ListAnswer extends Answer {
		public List<String> listAnswers = new ArrayList<String>();
		public List<String> listDocs = new ArrayList<String>();
	}
	public static class SingleAnswer extends Answer {
		public String answer = null;
		public String doc = null;
	}
}
