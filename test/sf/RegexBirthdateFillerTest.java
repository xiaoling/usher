package sf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import sf.filler.regex.RegexPerDateOfBirthFiller;
import tackbp.KbEntity.EntityType;

/**
 * 
 * @author Xiao Ling
 */

public class RegexBirthdateFillerTest {

	@Test
	public void testPattern() throws Exception {
		// make up a query
		SFEntity sfm = new SFEntity();
		sfm.mentionString = "Raul Castro";
		sfm.queryId = "E00001";
		sfm.entityType = EntityType.PER;

		// scan a line
		RegexPerDateOfBirthFiller filler = new RegexPerDateOfBirthFiller();
		Map<String, String> annotations = new HashMap<String, String>();
		annotations.put(SFConstants.TOKENS, "795374  Raul Castro was born on June 3 , 1931 in Cuba 's Oriente Province and educated at Jesuit schools and the University of Havana .");
		annotations.put(SFConstants.META, "795374\t0\tXIN_ENG\t5");
		filler.predict(sfm, annotations);

		assertEquals("June 3", sfm.answers.get(filler.slotName).toString());
	}

}
