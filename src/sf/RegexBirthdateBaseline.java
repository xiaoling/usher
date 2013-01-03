package sf;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tackbp.KbEntity.EntityType;

public class RegexBirthdateBaseline {
	public static final String slotName = "per:date_of_birth";

	public void predict(SFEntityMention mention, List<String> lines,
			String filename) {
		if (mention.ignoredSlots.contains(slotName)
				|| mention.entityType != EntityType.PER) {
			return;
		}

		boolean relevant = false;
		for (String line : lines) {
			if (line.contains(mention.mentionString)) {
				relevant = true;
				break;
			}
		}

		if (!relevant) {
			return;
		}

		Pattern patternFullName = Pattern.compile(mention.mentionString
				+ " was born (in|on) (.+?)\\p{Punct}");

		String[] names = mention.mentionString.split(" ");
		String first = names[0];
		String last = null;
		if (names.length > 1) {
			last = names[names.length - 1];
		} else {
			last = first;
		}

		Pattern patternFirstName = Pattern.compile(first
				+ " was born (in|on) (.+?)\\p{Punct}");

		Pattern patternLastName = Pattern.compile(last
				+ " was born (in|on) (.+?)\\p{Punct}");

		for (String line : lines) {
			{
				Matcher matcher = patternFullName.matcher(line);
				if (matcher.find()) {
					SFEntityMention.SingleAnswer ans = new SFEntityMention.SingleAnswer();
					ans.answer = matcher.group(2);
					ans.doc = filename;
					mention.answers.put(slotName, ans);
				}
			}
			{
				Matcher matcher = patternFirstName.matcher(line);
				if (matcher.find()) {
					SFEntityMention.SingleAnswer ans = new SFEntityMention.SingleAnswer();
					ans.answer = matcher.group(2);
					ans.doc = filename;
					mention.answers.put(slotName, ans);
				}
			}
			{
				Matcher matcher = patternLastName.matcher(line);
				if (matcher.find()) {
					SFEntityMention.SingleAnswer ans = new SFEntityMention.SingleAnswer();
					ans.answer = matcher.group(2);
					ans.doc = filename;
					mention.answers.put(slotName, ans);
				}
			}
		}
	}
}