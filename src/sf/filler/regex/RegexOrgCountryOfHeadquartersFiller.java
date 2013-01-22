package sf.filler.regex;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * @author hunlan lin
 */
public class RegexOrgCountryOfHeadquartersFiller extends Filler {
    public static final String SLOT_NAME = "org:country_of_headquarters";
    public static final String US_STRING = "U.S.";
    public static final String[] US_STATES = 
    {"Alabama", "Alaska", "American Samoa", "Arizona", "Arkansas", 
     "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", 
     "Florida", "Georgia", "Guam", "Hawaii", "Idaho", "Illinois", 
     "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", 
     "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", 
     "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", 
     "New Jersey", "New Mexico", "New York", "North Carolina", 
     "North Dakota", "Northern Marianas Islands", "Ohio", "Oklahoma",
     "Oregon", "Pennsylvania", "Puerto Rico", "Rhode Island", 
     "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", 
     "Vermont", "Virginia", "Virgin Islands", "Washington", "West Virginia", 
     "Wisconsin", "Wyoming", "Calif.", "N.J."};

    public RegexOrgCountryOfHeadquartersFiller() {
	slotName = SLOT_NAME;
    }

    @Override
    public void predict(SFEntity mention, Map<String, String> annotations) {
	// the query needs to be a ORG type.
	if (mention.ignoredSlots.contains(slotName) 
	        || mention.entityType != EntityType.ORG) {
	    return;
	}

	// check if the entity is mentioned from .token
	String tokens = annotations.get(SFConstants.TOKENS);
	if (tokens == null
	        || !tokens.contains(mention.mentionString)) {
	    return;
	}

	// get filename from .meta
	String[] meta = annotations.get(SFConstants.META).split("\t");
	String filename = meta[2];

	// Country Name
	String orgName = mention.mentionString;

	// Pattern Definition
	// Headquarter pattern match (didn't work at all...)
	Pattern pattern_orgHQPrep = 
	    Pattern.compile(" (headquarter|headquarters|headquartered) (in|at) (.+?)\\p{Punct}");
	
	Pattern pattern_basedIn = 
	    Pattern.compile(" (base in|based in) (\\w*\\s?)([A-Z].+?)\\p{Punct}");

	Pattern pattern_orgHQOf =
	    Pattern.compile(orgName + " of (\\w*\\s?)([A-Z].+?)\\p{Punct}");

	// Apply the pattern
	{
	    Matcher matcher = pattern_orgHQPrep.matcher(tokens);
	    if (matcher.find()) {
		String orgCountry = matcher.group(3).trim();
		// Check if its US
		for (String s : US_STATES) {
		    if (tokens.contains(s)) {
			orgCountry = US_STRING;
			break;
		    }
		}

		SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
		ans.answer = orgCountry;
		ans.doc = filename;
		mention.answers.put(this.slotName, ans);
	    }
	}

	{
	    Matcher matcher = pattern_basedIn.matcher(tokens);
	    if (matcher.find()) {
		String orgCountry = matcher.group(3).trim();
		// Check if its US
		for (String s : US_STATES) {
		    if (tokens.contains(s)) {
			orgCountry = US_STRING;
			break;
		    }
		}
		
		SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
		ans.answer = orgCountry;
		ans.doc = filename;
		mention.answers.put(this.slotName, ans);
	    }
	}

	{
	    Matcher matcher = pattern_orgHQOf.matcher(tokens);
	    if (matcher.find()) {
		String orgCountry = matcher.group(2).trim();
		// Check if its US                                                                                                                                                                                                            
		for (String s : US_STATES) {
                    if (tokens.contains(s)) {
			orgCountry = US_STRING;
			break;
                    }
		}

                SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
		ans.answer = orgCountry;
		ans.doc = filename;
		mention.answers.put(this.slotName, ans);

	    }
	}
	
    }
}