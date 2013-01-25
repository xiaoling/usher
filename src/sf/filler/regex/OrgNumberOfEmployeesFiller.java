package sf.filler.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

public class OrgNumberOfEmployeesFiller extends Filler {

	public OrgNumberOfEmployeesFiller() {
		slotName = "org:number_of_employees/members";
	}
	
	@Override
	public void predict(SFEntity mention, Map<String, String> annotations) {
		//System.out.println("et: " + mention.entityType.toString());
		// the query needs to be a PER type.
		//System.out.println("mention: " + mention.mentionString);
		Set<String> keys = annotations.keySet();
		//System.out.println("key: " + SFConstants.TOKENS);
		//System.out.println("a: " + annotations.get(SFConstants.TOKENS));
		if(mention.mentionString.contains("Workers Union")) {
			System.out.println("SFE: " + mention.mentionString);
		}
		
		if (mention.ignoredSlots.contains(slotName)
				|| mention.entityType != EntityType.ORG) {
			return;
		}

		// check if the entity is mentioned.
		String tokens = annotations.get(SFConstants.TOKENS);
		String[] names = mention.mentionString.split(" ");
		if (!tokens.contains(names[names.length - 1])) {
			return;
		}

		// get the filename of this sentence.
		String[] meta = annotations.get(SFConstants.META).split("\t");
		String filename = meta[2];


		String first = names[0];
		String last = null;
		if (names.length > 1) {
			last = names[names.length - 1];
		} else {
			last = first;
		}

		List<Pattern> patterns = new ArrayList<Pattern>();
		// Three patterns are used here beginning with the full name, the first name and the last name respectively.  
		String[] allnames = new String[3];
		allnames[0] = mention.mentionString;
		allnames[1] = first;
		allnames[2] = last;
		String[] professions = {"employees", "workers", "members", "students", 
										"people", "women", "men", "children"};
		String[] people = {"people", "women", "men", "children"};
		String word = "(\\w+?)";
		
		//Does a very specific scan of keywords regarding whether an organization has people working for them
		{
		for(int i = 0; i < allnames.length; i++) {	
			for(int j = 0; j < professions.length; j++) {

				patterns.add(Pattern.compile(allnames[i]
						+ " has ([0-9|,]+?) " + professions[j]));
				
				patterns.add(Pattern.compile(allnames[i]
						+ " has (\\w+?) ([0-9|,]+?) " + professions[j]));
				
				patterns.add(Pattern.compile(allnames[i]
						+ " has (\\w+?) ([0-9|,]+?) (\\w+?) " + professions[j]));
				
				patterns.add(Pattern.compile(allnames[i]
						+ " employed ([0-9|,]+?) " + professions[j]));
				
				patterns.add(Pattern.compile(allnames[i]
						+ " employed (\\w+?) ([0-9|,]+?) " + professions[j]));
				
				patterns.add(Pattern.compile(allnames[i]
						+ " (\\w+?) employs ([0-9|,]+?) " + professions[j]));
				
				patterns.add(Pattern.compile(allnames[i]
						+ " employs (\\w+?) ([0-9|,]+?) " + professions[j]));
				
				patterns.add(Pattern.compile(allnames[i]
						+ " (\\w+?) (\\w+?) employs ([0-9|,]+?) " + professions[j]));
			}
		}
		

		int length = patterns.size();
		//if(tokens.contains("1757645")) {
		//	System.out.println(tokens);
		//}
		for(int i = 0; i < length; i++)
		{
			// apply the pattern using the full name
			Matcher matcher = patterns.get(i).matcher(tokens);
			if (matcher.find()) {
				//System.out.println(tokens);
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				String res = matcher.group(0).trim();
				ans.answer = getNumber(res);
				//System.out.println("ans: " + filename + " " + ans.answer); 
				ans.doc = filename;
				mention.answers.put(slotName, ans);
				return;
			}
		}
		
		}
		
		//A test to extract all "-members" words

		for(int i = 0; i < allnames.length; i++) {	
			Pattern compiled = Pattern.compile("([0-9|,]+?)-member " + allnames[i]);
			
			Matcher matcher = compiled.matcher(tokens);
			if (matcher.find() && !mention.answers.containsKey(slotName)) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				
				String res = matcher.group(0).trim();
				ans.answer = getNumber(res);
				//System.out.println("ans: " + filename + " " + ans.answer); 
				ans.doc = filename;
				mention.answers.put(slotName, ans);
				return;
			}
		}
		
		
		//Extracts the sentence employs some number of people
		for(int j = 0; j < professions.length; j++) {
			Pattern compiled = Pattern.compile(" employs (\\w+?) ([0-9|,]+?) " + professions[j]);
			Matcher matcher = compiled.matcher(tokens);
			if (matcher.find() && !mention.answers.containsKey(slotName)) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				
				String res = matcher.group(0).trim();
				ans.answer = getNumber(res);
				//System.out.println("ans: " + filename + " " + ans.answer); 
				//System.out.println("ans: " + filename + " " + res); 
				ans.doc = filename;
				mention.answers.put(slotName, ans);
				return;
			}
		}
		
		//patterns.add(Pattern.compile(" ([0-9|,]+?) " + "employees"));
		{
			Pattern compiled = Pattern.compile(" ([0-9|,]+?) " + "employees");
			Matcher matcher = compiled.matcher(tokens);
			if (matcher.find() && !tokens.contains("laid off") && !mention.answers.containsKey(slotName)) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				String res = matcher.group(0).trim();
				ans.answer = getNumber(res);
				//System.out.println("ans: " + filename + " " + ans.answer); 
				ans.doc = filename;
				mention.answers.put(slotName, ans);
				return;
			}
		}
		

	}
	
	private String getNumber(String input) {
		String res = input.trim();
		int len = res.length();
		StringBuffer sb = new StringBuffer();
		for(int j = 0; j < len; j++) {
			char c = res.charAt(j);
			if((c >= '0' && c <= '9') || (c == ',' && sb.length() != 0)) {
				sb.append(c);
			} else if (sb.length() != 0) {
				break;
			}
		}
		return sb.toString();
	}
}
