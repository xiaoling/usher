package sf.filler.regex;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.HashSet;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

import util.FileUtil;

/**
 * Needs "tokens", "meta",
 * @author xiaoling
 *
 */
public class RegexPerStateorprovinceOfBirthFiller extends Filler {

	public RegexPerStateorprovinceOfBirthFiller() {
		slotName = "per:stateorprovince_of_birth";
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

		// get a set of countries
		// Currently just stored the file of the countries in the same folder.
		// TODO move the countries file somewher else and change the path to the file.
		String fileString = FileUtil.getTextFromFile("data/Countries.txt");
		String[] countries = fileString.split("\n");
		Set<String> countrySet = new HashSet<String>();

		for(String country : countries) {
			countrySet.add(country);
		}

		// Three patterns are used here beginning with the full name, the first name and the last name respectively.  
		Pattern patternFullName1 = Pattern.compile(mention.mentionString
				+ " was born in ([A-Z][ a-zA-Z]+?)\\p{Punct}");
		
		Pattern patternFirstName1 = Pattern.compile(first
				+ " was born in ([A-Z][ a-zA-Z]+?)\\p{Punct}");

		Pattern patternLastName1 = Pattern.compile(last
				+ " was born in ([A-Z][ a-zA-Z]+?)\\p{Punct}");

		Pattern patternFullName2 = Pattern.compile(mention.mentionString
				+ " was born in ([A-Z][ a-zA-Z]+?), ([A-Z][ a-zA-Z]+?)\\p{Punct}");

		Pattern patternFirstName2 = Pattern.compile(first
				+ " was born in ([A-Z][ a-zA-Z]+?), ([A-Z][ a-zA-Z]+?)\\p{Punct}");

		Pattern patternLastName2 = Pattern.compile(last
				+ " was born in ([A-Z][ a-zA-Z]+?), ([A-Z][ a-zA-Z]+?)\\p{Punct}");

		Pattern patternFullName3 = Pattern.compile(mention.mentionString
				+ " was born in ([A-Z][ a-zA-Z]+?), ([A-Z][ a-zA-Z]+?), ([A-Z][ a-zA-Z]+?)\\p{Punct}");

		Pattern patternFirstName3 = Pattern.compile(first
				+ " was born in ([A-Z][ a-zA-Z]+?), ([A-Z][ a-zA-Z]+?), ([A-Z][ a-zA-Z]+?)\\p{Punct}");

		Pattern patternLastName3 = Pattern.compile(last
				+ " was born in ([A-Z][ a-zA-Z]+?), ([A-Z][ a-zA-Z]+?), ([A-Z][ a-zA-Z]+?)\\p{Punct}");

		Pattern patternFullNameWithYear = Pattern.compile(mention.mentionString
				+ " was born (in|on) (.+?) in (.+?)( and|[\\.\\?;:,])");

		Pattern patternFirstNameWithYear = Pattern.compile(first
				+ " was born (in|on) (.+?) in (.+?)( and|[\\.\\?;:,])");

		Pattern patternLastNameWithYear = Pattern.compile(last
				+ " was born (in|on) (.+?) in (.+?)( and|[\\.\\?;:,])");

		Pattern patternFullNameWithYearEnd = Pattern.compile("Born in (.+?) in (.+?), " + mention.mentionString);

		Pattern patternFirstNameWithYearEnd = Pattern.compile("Born in (.+?) in (.+?), " + first);

		Pattern patternLastNameWithYearEnd = Pattern.compile("Born in (.+?) in (.+?), " + last);

		{
			// apply the pattern using the full name
			Matcher matcher = patternFullName1.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(1).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the first name
			Matcher matcher = patternFirstName1.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(1).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the last name
			Matcher matcher = patternLastName1.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(1).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the full name
			Matcher matcher = patternFullName2.matcher(tokens);
			if (matcher.find()) {
				if(countrySet.contains(matcher.group(2).trim())) {
					SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
					ans.answer = matcher.group(2).trim();
					ans.doc = filename;
					mention.answers.put(slotName, ans);
				} else {
					SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
					ans.answer = matcher.group(1).trim();
					ans.doc = filename;
					mention.answers.put(slotName, ans);
				}
			}
		}
		{
			// apply the pattern using the first name
			Matcher matcher = patternFirstName2.matcher(tokens);
			if (matcher.find()) {
				if(countrySet.contains(matcher.group(2).trim())) {
					SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
					ans.answer = matcher.group(2).trim();
					ans.doc = filename;
					mention.answers.put(slotName, ans);
				} else {
					SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
					ans.answer = matcher.group(1).trim();
					ans.doc = filename;
					mention.answers.put(slotName, ans);
				}
			}
		}
		{
			// apply the pattern using the last name
			Matcher matcher = patternLastName2.matcher(tokens);
			if (matcher.find()) {
				if(countrySet.contains(matcher.group(2).trim())) {
					SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
					ans.answer = matcher.group(2).trim();
					ans.doc = filename;
					mention.answers.put(slotName, ans);
				} else {
					SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
					ans.answer = matcher.group(1).trim();
					ans.doc = filename;
					mention.answers.put(slotName, ans);
				}
			}
		}
		{
			// apply the pattern using the full name
			Matcher matcher = patternFullName3.matcher(tokens);
			if (matcher.find() && countrySet.contains(matcher.group(3).trim())) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(2).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the first name
			Matcher matcher = patternFirstName3.matcher(tokens);
			if (matcher.find() && countrySet.contains(matcher.group(3).trim())) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(2).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the last name
			Matcher matcher = patternLastName3.matcher(tokens);
			if (matcher.find() && countrySet.contains(matcher.group(3).trim())) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(2).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the full name
			Matcher matcher = patternFullNameWithYear.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(3).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the full name
			Matcher matcher = patternFirstNameWithYear.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(3).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the full name
			Matcher matcher = patternLastNameWithYear.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(3).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the full name
			Matcher matcher = patternFullNameWithYearEnd.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(1).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the full name
			Matcher matcher = patternFirstNameWithYearEnd.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(1).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
		{
			// apply the pattern using the full name
			Matcher matcher = patternLastNameWithYearEnd.matcher(tokens);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = matcher.group(1).trim();
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
	}
	
}
