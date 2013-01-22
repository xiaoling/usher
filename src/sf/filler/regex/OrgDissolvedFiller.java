package sf.filler.regex;

import java.util.Map;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * Needs "tokens", "meta", "text", "stanfordner"
 * @author jennyabrahamson 
 *
 */
public class OrgDissolvedFiller extends Filler {

	public OrgDissolvedFiller() {
		slotName = "org:dissolved";
	}
	
	@Override
	public void predict(SFEntity mention, Map<String, String> annotations) {

		// the query needs to be an ORG type.
		if (mention.ignoredSlots.contains(slotName)
        || mention.entityType != EntityType.ORG) {
			return;
		}

    // the sentence must contain a date and an organization
    String stanfordNer = annotations.get(SFConstants.STANFORDNER);
    if (!(stanfordNer.contains("DATE") && stanfordNer.contains("ORGANIZATION"))) {
      return;
    }

    // the sentence must contain the word dissolved or closed down
		String text = annotations.get(SFConstants.TEXT);
    if (!text.contains("dissolved")) {
      return;
    }


    // construct the date
    String[] tokens = annotations.get(SFConstants.TOKENS).split("\t")[1].split(" ");
    String[] parsedNer = stanfordNer.split("\t")[1].split(" ");
    String date = ""; 
    for (int i = 0; i < parsedNer.length; i++) {
      if (!parsedNer[i].contains("DATE") && date.length() == 0) {
        continue;
      }

      if (!parsedNer[i].contains("DATE") && date.length() > 0) {
        break;
      }

      date += tokens[i] + " ";
    }

    // NOTE(jennya): using this strategy I couldn't come up with ANY cases that
    // worked. There are only 3 orgs/dissolved date pairs that I should be able
    // to link, and after examining candidate sentences for 2 of those pairs, I
    // found that it is impossible to link the correct organization without
    // entity linking. IE Jewish National Fund --> JNF, Pamela Martin &
    // Associates --> 'The operation'. So rather than find 0 cases that work
    // I'll generate a number of false positives.
		// check if the entity is mentioned in this sentence's doc.
		//if (!filename.contains(mention.mentionDoc)) {
	  //		return;
		//} 

		String[] meta = annotations.get(SFConstants.META).split("\t");
		String filename = meta[2];

    SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
    ans.answer = date;
    ans.doc = filename;
    mention.answers.put(slotName, ans);
    System.out.println(mention.mentionString + " --> " + date);
	}
}
