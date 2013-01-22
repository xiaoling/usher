package sf.filler.regex;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * Needs "tokens", "meta",
 * @author xiaoling
 *
 */
public class RegexPerCountryOfBirth extends Filler {

	public RegexPerCountryOfBirth() {
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
		// System.out.println(mention.mentionString);
		if (!tokens.contains(mention.mentionString)) {
			return;
		}

		// get the filename of this sentence.
		String[] meta = annotations.get(SFConstants.META).split("\t");
		String filename = meta[2];

		String[] names = mention.mentionString.split(" ");
		String full_name = mention.mentionString;
		String first = names[0];
		String last = null;
		if (names.length > 1) {
			last = names[names.length - 1];
		} else {
			last = first;
		}
		
		Pattern pattern_month = Pattern.compile("(Jan|Feb|March|April|May|Jun|Jul|Aug|Sept|Nov|Dec)");
		Pattern pattern_startWithNumber = Pattern.compile("[0-9]");
		/*
		For pattern 1
		*/
		{
			// Search for any sentence with the word "born" on it
			String keyword1 = "born";
			// That also have the word "in"
			String keyword2 = " in ";
			int keyword2_length = keyword2.length();
			
			// Look for "born"
			Pattern patterns = Pattern.compile(keyword1);
			Matcher matcher = patterns.matcher(tokens);
			if(matcher.find())
			{
				{
					boolean DEBUG = false;
					if(DEBUG){
						System.out.println(full_name + " : ");
						System.out.println(tokens);
						System.out.println();
					}
				}
				
				// Search for the word "in"
				// that is not followed by number, this is to eliminate
				// the case of born in 'year'
				// and also those that are not followed by month name
				String mod_token = tokens;
				if(mod_token.contains(keyword2))
				{
					int idx = -1;
					while((idx = mod_token.indexOf(keyword2)) > 0){
						mod_token = mod_token.substring(idx + keyword2_length).trim();
						
						Matcher matcher1 = pattern_month.matcher(mod_token);
						Matcher matcher2 = pattern_startWithNumber.matcher(mod_token);
						// It's not a month or year
						if(!matcher1.find() && !matcher2.find()){
							if(mod_token.contains(" ")){
								mod_token = mod_token.substring(0, mod_token.indexOf(" "));
							}
							
							boolean DEBUG = true;
							if(DEBUG){
								System.out.println("ANSWER: " + mod_token);
								System.out.println();
							}
							
							SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
							ans.answer = mod_token.trim();
							ans.doc = filename;
							mention.answers.put(slotName, ans);
							return;
						}
					}
				}
			}
		}
	}
	
}
