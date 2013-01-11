import java.io.IOException;
import java.util.List;

import sf.SFConstants;
import sf.SFEntity;
import sf.SFEntity.SingleAnswer;
import sf.eval.SFScore;
import sf.filler.RegexBirthdateFiller;
import sf.filler.RegexBirthdateBaselineTrueFileList;
import sf.retriever.Corpus;
import sf.retriever.FakeCorpus;
import tackbp.KB;
import tackbp.RetrieveDocument;
import util.io.FileUtil;
import el.ElConstants;
import el.EntityMention;
import el.Eval;
import el.MilneNEL;

public class Main {
	public static void main(String[] args) {
		if (args[0].equals("sf")) {
			sf_main(args);
		}
		if (args[0].equals("el")) {
			el_main(args);
		}
	}

	public static void sf_main(String[] args) {
		boolean run = false;
		boolean eval = false;
		if (args.length == 2) {
			if (args[1].equals("all")) {
				run = true;
				eval = true;
			} else if (args[1].equals("run")) {
				run = true;
			} else if (args[1].equals("eval")) {
				eval = true;
			}
		}

		if (run) {
			sf.query.QueryReader qReader = new sf.query.QueryReader();
			qReader.readFrom(SFConstants.queryFile);
			RegexBirthdateFiller baseline = new RegexBirthdateFiller();
//			Corpus corpus = new Corpus();
			Corpus corpus = new FakeCorpus(RegexBirthdateBaselineTrueFileList.files);
			String file = corpus.next();
			int c = 0;
			while (file != null) {
				if (c++ % 1000 == 0) {
					System.out.print("finished reading .." + c + "\r");
				}
				file = file.replace(".sgm", "");
				List<String> lines = RetrieveDocument.getContent(file);
				for (SFEntity mention : qReader.queryList) {
					baseline.predict(mention, lines, file);
				}
				file = corpus.next();
			}

			// System.out.println("baseline hit = " + baseline.hit);
			StringBuilder sb = new StringBuilder();
			for (SFEntity mention : qReader.queryList) {
				if (mention.answers
						.containsKey(RegexBirthdateFiller.slotName)) {
					// Column 1: query id
					// Column 2: the slot name
					// Column 3: a unique run id for the submission
					// Column 4: NIL, if the system believes no information is
					// learnable for this slot. Or, a single docid
					// which supports the slot value
					// Column 5: a slot value
					SingleAnswer ans = (SingleAnswer) mention.answers
							.get(RegexBirthdateFiller.slotName);
					sb.append(String.format("%s\t%s\t%s\t%s\t%s\n",
							mention.queryId, RegexBirthdateFiller.slotName,
							"RegexBirthdateBaseline", ans.doc, ans.answer));
				} else {
					sb.append(String.format("%s\t%s\t%s\t%s\t%s\n",
							mention.queryId, RegexBirthdateFiller.slotName,
							"RegexBirthdateBaseline", "NIL", ""));
				}
			}
			FileUtil.writeTextToFile(sb.toString(),
					"data/sf_predictions/sf.out");
		}
		if (eval) {
			try {
//				SFGold.getGoldFromAssessment();
				SFScore.main(new String[] { "data/sf_predictions/sf.out",
						"data/sf.gold" });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void el_main(String[] args) {
		boolean run = false;
		boolean eval = false;
		if (args.length == 2) {
			if (args[1].equals("all")) {
				run = true;
				eval = true;
			} else if (args[1].equals("run")) {
				run = true;
			} else if (args[1].equals("eval")) {
				eval = true;
			}
		}
		if (run) {
			KB kb = new KB();
//			kb.init();
			el.QueryReader qReader = new el.QueryReader();
			qReader.readFrom(ElConstants.queryFile);
//			StringMatchBaseline baseline = new StringMatchBaseline();
			MilneNEL baseline = new MilneNEL();
			for (EntityMention mention : qReader.queryList) {
				baseline.predict(mention, kb);
			}
			StringBuilder sb = new StringBuilder();
			for (EntityMention mention : qReader.queryList) {
				sb.append(mention.queryId + "\t" + mention.entityId + "\n");
			}
//			FileUtil.writeTextToFile(sb.toString(),
//					"data/el_predictions/el.out");
		}
		if (eval) {
			System.out.println("Eval...");
			Eval.run();
		}
	}

	public static void tsf_main(String[] args) {
		String doc = "AFP_ENG_20070129.0462.LDC2009T13";
		String[] pieces = doc.split("_|\\.");
		String file = pieces[0].toLowerCase() + "_" + pieces[1].toLowerCase()
				+ "/" + pieces[2] + "/" + doc + ".sgm";
		String text = FileUtil
				.getTextFromFile("/homes/gws/xiaoling/dataset/TAC10Source/TAC_2010_KBP_Source_Data/data/2009/nw/"
						+ file);
		String arg1 = "pianist";
		String arg2 = "Sutil";

		String[] lines = text.split("\n");
		System.out.println(text);
		System.out.println("(*************)");

		for (String line : lines) {
			if (line.contains(arg1) || line.contains(arg2)) {
				System.out.println(line);
			}
		}
	}
}
