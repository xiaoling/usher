package sf.filler.regex;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

public class PerCountryOfBirthFiller extends Filler {

	public PerCountryOfBirthFiller() {
		slotName = "per:country_of_birth";
	}

	private static int CountWhitespaceInStr(String str) {
		int count = 0;
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			if (ch == ' ' || ch == '\t') {
				++count;
			}
		}
		return count;
	}

	private static int GetTokenStartFromIndex(String str, int index) {
		return CountWhitespaceInStr(str.substring(0, index));
	}

	private static int GetTokenEndFromIndex(String str, int index) {
		str = str.substring(0, index);
		if (str.endsWith(" ")) {
			str = str.substring(0, str.length() - 1);
		}
		return 1 + CountWhitespaceInStr(str);
	}

	private String MergeArray(String strs[], int start, int end) {
		String ret = "";
		for (int i = start; i < end; ++i) {
			if (i > 0) {
				ret += " ";
			}
			ret += strs[i];
		}
		return ret;
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

		String ner = annotations.get(SFConstants.STANFORDNER);
		String token_list[] = tokens.split("\\s");

		
		if (
				(tokens.contains("Susan Boyle") /*&& tokens.contains("Scotland")*/) ||
				(tokens.contains("Alexandra Burke") /*&& tokens.contains("England")*/) ||
				(tokens.contains("Paul Sculfor") /*&& tokens.contains("Great Britain")*/)
		) {
			System.out.println("POTENTIAL: " + tokens);
			//System.out.println("'-> NER: " + ner);
		}

		// get the filename of this sentence.
		String[] meta = annotations.get(SFConstants.META).split("\t");
		String filename = meta[2];

		// Match things like PERSON is from
		Pattern patternIsFrom = Pattern.compile(", " + mention.mentionString
				+ " , is from (.+?)\\p{Punct}");
		{
			// apply the pattern using the full name
			Matcher matcher = patternIsFrom.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(1).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
				System.out.println("FOUND: " + tokens);
			}
		}

		// Match things like COUNTRY's PERSON e.x. England's Lewis Hamilton
		Pattern nerPattern = Pattern.compile("(LOCATION\\s)+O (PERSON\\s?)+");
		{
			Matcher matcher = nerPattern.matcher(ner);
			while (matcher.find()) {
				int tokStart = GetTokenStartFromIndex(ner, matcher.start());
				int tokEnd = GetTokenEndFromIndex(ner, matcher.end());

				String subToks = MergeArray(token_list, tokStart, tokEnd);

				// Now that we have a correct ner sequence, regex match the
				// exprmatcher.matches()) {
				Pattern regex = Pattern.compile("([^']+) 's "
						+ mention.mentionString);
				Matcher matcher2 = regex.matcher(subToks);
				if (matcher2.matches()) {
					//System.out.println("LOK: {{" + subToks + "}}");

					SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
					ans.answer = matcher2.group(1).trim();
					ans.doc = filename;
					mention.answers.put(slotName, ans);
				}
			}
		}
	}
}