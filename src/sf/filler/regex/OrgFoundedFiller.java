package sf.filler.regex;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * CSE 454 - Assignment 1: Slot Filling
 * @author James Lee
 */
public class OrgFoundedFiller extends Filler {

	public OrgFoundedFiller() {
		slotName = "org:founded";
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

		Pattern pattern1 = Pattern.compile(mention.mentionString
			+ " was founded (in|on) (.+?)\\p{Punct}");

		Pattern pattern2 = Pattern.compile(mention.mentionString
			+ ", founded in (.+?)\\p{Punct}");

		Pattern pattern3 = Pattern.compile(mention.mentionString
			+ " was established (in|on) (.+?)\\p{Punct}");

		Pattern pattern4 = Pattern.compile(mention.mentionString
			+ ", established in (.+?)\\p{Punct}");

		{
			Matcher matcher = pattern1.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(2).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			Matcher matcher = pattern2.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(2).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			Matcher matcher = pattern3.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(2).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			Matcher matcher = pattern4.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(2).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
	}
	
}
