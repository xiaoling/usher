package sf;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexBirthdateBaseline {
	public static final String slotName = "per:date_of_birth";
	public void predict(SFEntityMention mention, List<String> lines, String filename) {
		if (mention.ignoredSlots.contains(slotName)){
			return ;
		}
		Pattern pattern = Pattern.compile(mention.mentionString+" was born in (.+?)\\p{Punct}");
		for (String line: lines) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				SFEntityMention.SingleAnswer ans = new SFEntityMention.SingleAnswer();
				ans.answer = matcher.group(1);
				ans.answer = filename;
				mention.answers.put(slotName, ans); 
			}
		}
	}
}