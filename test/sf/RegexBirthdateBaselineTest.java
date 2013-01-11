package sf;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import sf.SFEntity.SingleAnswer;
import sf.filler.RegexBirthdateFiller;
import sf.retriever.raw.Corpus;
import sf.retriever.raw.FakeCorpus;
import tackbp.KbEntity.EntityType;
import tackbp.RetrieveDocument;

/**
 * 
 * @author Xiao Ling
 */

public class RegexBirthdateBaselineTest {

	@Test
	public void testPattern() {
		// make up a query
		sf.query.QueryReader qReader = new sf.query.QueryReader();
		SFEntity sfm = new SFEntity();
		sfm.mentionString = "Raul Castro";
		sfm.queryId = "E00001";
		sfm.entityType = EntityType.PER;
		qReader.queryList = new ArrayList<SFEntity>();
		qReader.queryList.add(sfm);
		
		// scan a specified file
		RegexBirthdateFiller baseline = new RegexBirthdateFiller();
		Corpus corpus = new FakeCorpus(new String[]{"XIN_ENG_20080224.0243.LDC2009T13.sgm"});
		String file = corpus.next();
		while (file != null) {
			file = file.replace(".sgm", "");
			List<String> lines = RetrieveDocument.getContent(file);
			for (SFEntity mention : qReader.queryList) {
				baseline.predict(mention, lines, file);
			}
			file = corpus.next();
		}

		assertEquals("June 3", qReader.queryList.get(0).answers.get(RegexBirthdateFiller.slotName));
		
//		// System.out.println("baseline hit = " + baseline.hit);
//		StringBuilder sb = new StringBuilder();
//		for (SFEntityMention mention : qReader.queryList) {
//			if (mention.answers.containsKey(RegexBirthdateBaseline.slotName)) {
//				// Column 1: query id
//				// Column 2: the slot name
//				// Column 3: a unique run id for the submission
//				// Column 4: NIL, if the system believes no information is
//				// learnable for this slot. Or, a single docid
//				// which supports the slot value
//				// Column 5: a slot value
//				SingleAnswer ans = (SingleAnswer) mention.answers
//						.get(RegexBirthdateBaseline.slotName);
//				sb.append(String.format("%s\t%s\t%s\t%s\t%s\n",
//						mention.queryId, RegexBirthdateBaseline.slotName,
//						"RegexBirthdateBaseline", ans.doc, ans.answer));
//			} else {
//				sb.append(String.format("%s\t%s\t%s\t%s\t%s\n",
//						mention.queryId, RegexBirthdateBaseline.slotName,
//						"RegexBirthdateBaseline", "NIL", ""));
//			}
//		}
//		System.out.println(sb.toString());
	}

}