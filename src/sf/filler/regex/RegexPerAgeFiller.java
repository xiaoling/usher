package sf.filler.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * 
 * 
 * @author Michael Hotan
 */
public class RegexPerAgeFiller extends Filler {

	// try to encompass all representation of a number in regex
	// NOTE: not completely case insensitive
	private static final String BACK_SLASH = "\\";
	// For digit representation
	private static final String numRep1 = BACK_SLASH + "d"; 
	// For written representation of number
	private static final String numRep2 = "\\b[a-zA-Z]*[tT][yY]\\b|" +
			"\\b[a-zA-Z]*teen\\b|\\b[a-zA-Z]*TEEN\\b"; 
	private static final String numRep3 = "one|two|three|four|five|six|seven" +
			"|eight|nine|ten|eleven|twelve";
	// Creates a grouping
	public static final String NUM_REGEX = "(" +numRep1 + "|" + numRep2 + "|" + numRep3 + ")";

	public RegexPerAgeFiller() {
		slotName = "per:age";
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
		if (!tokens.toLowerCase().contains(mention.mentionString.toLowerCase())) {
			return;
		}

		// get the filename of this sentence.
		String[] meta = annotations.get(SFConstants.META).split("\t");
		String filename = meta[2];

		String[] names = mention.mentionString.split(" ");
		String first = names[0];
		String last = null;
		if (names.length > 1) {
			last = names[names.length - 1];
		} else {
			last = first;
		}
		
		// Create all patterns for potential slots
		// Patterns are based on the occasion of key words
		// that reference age.
		// Maps regex expression to group number containing age
		HashMap<String, Integer> regex_strings = new HashMap<String, Integer>();
		// NOTE: My current assumption is that sentences usually include one person 
		// when they mention age.
		regex_strings.put("age (of |)" + NUM_REGEX, 2);
		regex_strings.put(NUM_REGEX + " (yr|years|yrs) old", 1);
		regex_strings.put(mention.mentionString + " is " + NUM_REGEX, 1);
		regex_strings.put(first + " is " + NUM_REGEX, 1);
		regex_strings.put(last + " is " + NUM_REGEX, 1);
		regex_strings.put(mention.mentionString + ", " + NUM_REGEX + ",", 1);
		regex_strings.put(first + ", " + NUM_REGEX + ",", 1);
		regex_strings.put(last + ", " + NUM_REGEX + ",", 1);
		// TODO: Add more regex string matches here or adjust current
		// ...
		
		// Create a Pattern for each built regex string
		for (String regex : regex_strings.keySet()) {
			Pattern pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			
			// apply the pattern using each regex string combination
			Matcher matcher = pat.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(regex_strings.get(regex)).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
	}

}
