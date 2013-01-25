package sf.filler.regex;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * Needs "tokens", "meta"
 * 
 * @author Eli White
 * 
 */
public class PerCountryOfBirthFiller2 extends Filler {

	public PerCountryOfBirthFiller2() {
		slotName = "per:country_of_birth";
	}

	@Override
	public void predict(SFEntity mention, Map<String, String> annotations) {
		// the query needs to be a PER type.
		if (mention.ignoredSlots.contains(slotName)
				|| mention.entityType != EntityType.PER) {
			return;
		}

		// check if the entity is mentioned.
		String tokens = annotations.get(SFConstants.TOKENS);
		if (!tokens.contains(mention.mentionString)) {
			return;
		}

		if (annotations.get(SFConstants.STANFORDNER) == null) {
			return;
		}

		String[] stanfordnerStr = annotations.get(SFConstants.STANFORDNER)
				.split("\t");

		if (stanfordnerStr == null) {
			return;
		}
		
		// This will contain the stanfordner data after the tab
		// We only have to do this try catch thing because sometimes there is bad
		// data and there is nothing after the tab in the original line		
		String[] stanfordner = new String[1];
		try {
			stanfordner = stanfordnerStr[1].split(" ");
		} catch (ArrayIndexOutOfBoundsException e) {
			return; // Stanfordner string doesn't have valid data for this entry
		}
		
		// The words in the sentence
		String[] sentence = tokens.split("\t")[1].split(" ");

		List<String> stanList = Arrays.asList(stanfordner);
		
		// get the filename of this sentence.
		String[] meta = annotations.get(SFConstants.META).split("\t");
		String filename = meta[2];
		
		// looking for born|native and a Location
		if ((tokens.contains("native") || tokens.contains("born")) && stanList.contains("LOCATION")) 
		{
			int index = stanList.indexOf("LOCATION");
			String location = sentence[index];
			
			
			SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
			ans.answer = location;
			ans.doc = filename;
			mention.answers.put(slotName, ans);
			
			return;
		}
	}
}
