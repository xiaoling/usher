package sf.filler.regex;

import java.util.*;
import java.util.regex.*;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * Needs "tokens", "meta",
 * @author xiaoling
 *
 */
public class OrgCountryOfHeadquartersFiller extends Filler {

	public OrgCountryOfHeadquartersFiller() {
		slotName = "org:country_of_headquarters";
	}
	
	@Override
	public void predict(SFEntity mention, Map<String, String> annotations) {
		// the query needs to be a ORG type.
		if (mention.ignoredSlots.contains(slotName)
				|| mention.entityType != EntityType.ORG) {
			return;
		}

		// check if the entity is mentioned.
		String tokens = annotations.get(SFConstants.TOKENS);
		if (!tokens.contains(mention.mentionString)) {
			return;
		}

		// get the filename of this sentence.
		String[] meta = annotations.get(SFConstants.META).split("\t");
		String filename = meta[2];
		
		String[] names = mention.mentionString.split(" ");
		
		String orgName = mention.mentionString;

		String first = names[0];
		String last = null;
		if (names.length > 1) {
			last = names[names.length - 1];
		} else {
			last = first;
		}

		ArrayList<Pattern> patterns = new ArrayList<Pattern>();
	
		// Three patterns are used here beginning with the full name, the first name and the last name respectively.  
		Pattern locatedInPattern = Pattern.compile(orgName+ " (.)* in");

		Pattern hqPattern = Pattern.compile("The headquarters of "+ orgName + " is in");

		patterns.add(locatedInPattern);
		patterns.add(hqPattern);

		for(int i = 0; i<patterns.size(); i++){
			Matcher matcher = patterns.get(i).matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				//System.out.println(matcher.group());
				ans.answer = matcher.group(1).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
	}
}
