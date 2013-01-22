package sf.filler.regex;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * Needs "tokens", "meta",
 * @author Justin McManus
 *
 */
public class PerDateOfDeathFiller2 extends Filler {

	public PerDateOfDeathFiller2() {
		slotName = "per:date_of_death";
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

		String regexDelimiter = "[-:\\/.,]";
		String regexDay = "((?:[0-2]?\\d{1})|(?:[3][01]{1}))";
		String regexMonth = "(?:([0]?[1-9]|[1][012])|(Jan(?:uary)?|Feb(?:ruary)?|Mar(?:ch)?|Apr(?:il)?|May|Jun(?:e)?|Jul(?:y)?|Aug(?:ust)?|Sep(?:tember)?|Sept|Oct(?:ober)?|Nov(?:ember)?|Dec(?:ember)?))";
		String regexYear = "((?:[1]{1}\\d{1}\\d{1}\\d{1})|(?:[2]{1}\\d{3}))";
		String regexEndswith = "(?![\\d])";

		// DD/MM/YYYY
		String regexDateEuropean = regexDay + regexDelimiter + regexMonth + regexDelimiter +
				       regexYear + regexEndswith;

		// MM/DD/YYYY
		String regexDateAmerican = regexMonth + regexDelimiter + regexDay + regexDelimiter +
				       regexYear + regexEndswith;

		// YYYY/MM/DD
		String regexDateTechnical = regexYear + regexDelimiter + regexMonth + regexDelimiter +
					regexDay + regexEndswith;

		List<String> nameList = new LinkedList<>();
		nameList.add(mention.mentionString);
		nameList.add(first);
		nameList.add(last);

		List<Pattern> patterns = new LinkedList<>();
		for (String name : nameList) {
			patterns.add(Pattern.compile(name + " died (in|on) (" + regexDateEuropean + ")"));
			patterns.add(Pattern.compile(name + " died (in|on) (" + regexDateAmerican + ")"));
			patterns.add(Pattern.compile(name + " died (in|on) (" + regexDateTechnical + ")"));
		}


		for (Pattern p : patterns) {
			Matcher matcher = p.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(2).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
	}

}
