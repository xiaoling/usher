package sf.filler.regex;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sf.SFConstants;
import sf.SFEntity;
import sf.filler.Filler;
import tackbp.KbEntity.EntityType;

/**
 * A slot filler for the website address of an organization.
 *
 * @author Dan Sanderson <dsanders@u.washington.edu>
 */
public class OrgWebsiteFiller extends Filler {

        public OrgWebsiteFiller() {
                slotName = "org:website";
        }

        @Override
        public void predict(SFEntity mention, Map<String, String> annotations) {
                // The query needs to be a ORG type.  I assume this is necessary
		// based on the RegexPerDateOfBirthFiller example.
                if (mention.ignoredSlots.contains(slotName)
                                || mention.entityType != EntityType.ORG) {
                        return;
                }

		String tokens = annotations.get(SFConstants.TOKENS);

		// Only consider sentences mentioning the full name of the org.
		// (I experimented with expanding this consideration, but did
		// not successfully improve the score.)
		if (!tokens.contains(mention.mentionString)) {
			return;
		}

                // Get the filename of this sentence, for result reporting.
                String[] meta = annotations.get(SFConstants.META).split("\t");
                String filename = meta[2];

		// Split the sentence field into individual tokens.
		String[] tokenFields = tokens.split("\t");
		String[] tokenList = tokenFields[1].split(" ");

		// Our heuristic pattern for web addresses intentionally excludes some
		// addresses allowed by the URI specification, and intentionally includes
		// domain names without URL schemes.  By this pattern, a web address:
		// * may begin with a URL scheme (http:// or https://)
		// * has a domain name, consisting of lowercase letters and dots;
		//     we match at least two letters to the right of the first dot to
		//     exclude abbreviations
		// * may contain more lowercase letters, dots, or forward slashes;
		//     some desired matches include URL paths
		Pattern website = Pattern.compile("^(https?://)?[a-z\\-]+\\.[a-z\\-]{2}[a-z\\-\\./]*$");

		for (String token : tokenList) {
			Matcher matcher = website.matcher(token);
			if (matcher.find()) {
				SFEntity.SingleAnswer ans = new SFEntity.SingleAnswer();
				ans.answer = token;
				ans.doc = filename;
				mention.answers.put(slotName, ans);
			}
		}
	}
}
