package sf.filler.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * Fills the State or Province of Headquarters slot.
 * 
 * Some problems:
 * - Colo. was judged incorrect for "Professional Rodeo Cowboys Association in Colorado Springs, Colo." sentence 3521852
 * 
 * @author adrogers@uw.edu (Andrew Rogers)
 */
public class OrgStateOrProvinceOfHeadquartersFiller extends Filler {
	
	public OrgStateOrProvinceOfHeadquartersFiller() {
		slotName = "org:stateorprovince_of_headquarters";
	}
	
	@Override
	public void predict(SFEntity mention, Map<String, String> annotations) {
		// the query needs to be a PER type.
		if (mention.ignoredSlots.contains(slotName)
				|| mention.entityType != EntityType.ORG) {
			return;
		}

		// check if the entity is mentioned
		String tokens = annotations.get(SFConstants.TOKENS);
		if (!tokens.contains(mention.mentionString) ) {
			return;
		}
		
		// check if there are both ORGANIZATION and LOCATION entities
		String ner = annotations.get(SFConstants.STANFORDNER);
		if (!ner.contains("ORGANIZATION") || !ner.contains("LOCATION")) {
			return;
		}
		
		String text = annotations.get(SFConstants.TEXT);
		
		// get the filename of this sentence.
		String[] meta = annotations.get(SFConstants.META).split("\t");
		String filename = meta[2];

		// company x in|of city, state[. ]
		{
			Pattern pattern = Pattern.compile(mention.mentionString + " (in|of) .+?, (.+?[\\.\\s,])");
			// apply the pattern using the full name
			Matcher matcher = pattern.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				String state = matcher.group(2).trim();
				ans.answer = state;
				ans.doc = filename;
				mention.answers.put(slotName, ans);
				System.out.println();
				System.out.println(tokens);
				System.out.println("entity: " + mention.mentionString);
				System.out.println("answer: " + ans.answer);
				System.out.println();
			}
		}
		
		// based in city, state,
		{
			Pattern pattern = Pattern.compile("[bB]ased in .+?, (.+?),");
			// apply the pattern using the full name
			Matcher matcher = pattern.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				String state = matcher.group(1).trim();
				ans.answer = state;
				ans.doc = filename;
				mention.answers.put(slotName, ans);
				System.out.println();
				System.out.println(tokens);
				System.out.println("entity: " + mention.mentionString);
				System.out.println("answer: " + ans.answer);
				System.out.println();
			}
		}
		
		// company x of city, state
	}
}
