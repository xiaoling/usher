package sf;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import sf.filler.RegexBirthdateFiller;
import tackbp.KbEntity.EntityType;

/**
 * 
 * @author Xiao Ling
 */

public class RegexBirthdateBaselineTest {

	@Test
	public void testPattern() {
		// make up a query
		SFEntity sfm = new SFEntity();
		sfm.mentionString = "Raul Castro";
		sfm.queryId = "E00001";
		sfm.entityType = EntityType.PER;

		// scan a specified file
		RegexBirthdateFiller filler = new RegexBirthdateFiller();
		Map<String, String> annotations = new HashMap<String, String>();
		annotations.put(SFConstants.TOKENS, "795374  Raul Castro was born on June 3 , 1931 in Cuba 's Oriente Province and educated at Jesuit schools and the University of Havana .");
		annotations.put(SFConstants.META, "795374\t0\tXIN_ENG\t5");
		filler.predict(sfm, annotations);

		assertEquals("June 3", sfm.answers.get(RegexBirthdateFiller.slotName).toString());

		// // System.out.println("baseline hit = " + baseline.hit);
		// StringBuilder sb = new StringBuilder();
		// for (SFEntityMention mention : qReader.queryList) {
		// if (mention.answers.containsKey(RegexBirthdateBaseline.slotName)) {
		// // Column 1: query id
		// // Column 2: the slot name
		// // Column 3: a unique run id for the submission
		// // Column 4: NIL, if the system believes no information is
		// // learnable for this slot. Or, a single docid
		// // which supports the slot value
		// // Column 5: a slot value
		// SingleAnswer ans = (SingleAnswer) mention.answers
		// .get(RegexBirthdateBaseline.slotName);
		// sb.append(String.format("%s\t%s\t%s\t%s\t%s\n",
		// mention.queryId, RegexBirthdateBaseline.slotName,
		// "RegexBirthdateBaseline", ans.doc, ans.answer));
		// } else {
		// sb.append(String.format("%s\t%s\t%s\t%s\t%s\n",
		// mention.queryId, RegexBirthdateBaseline.slotName,
		// "RegexBirthdateBaseline", "NIL", ""));
		// }
		// }
		// System.out.println(sb.toString());
	}

}
