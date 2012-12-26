import util.io.FileUtil;
import el.ElContants;
import el.EntityMention;
import el.KB;
import el.StringMatchBaseline;

public class Main {
	public static void main(String[] args) {
		// KB kb = new KB();
		// kb.init();
		// el.QueryReader qReader = new el.QueryReader();
		// qReader.readFrom(ElContants.queryFile);
		// StringMatchBaseline baseline = new StringMatchBaseline();
		// for (EntityMention mention: qReader.queryList) {
		// baseline.predict(mention, kb);
		// }
		// System.out.println("baseline hit = "+baseline.hit);
		// StringBuilder sb = new StringBuilder();
		// for (EntityMention mention: qReader.queryList) {
		// sb.append(mention.queryId+"\t"+mention.entityId+"\n");
		// }
		// FileUtil.writeTextToFile(sb.toString(), "el.out");

		System.out.println("Eval...");
		String[] evalLines = FileUtil.getTextFromFile(ElContants.evalFile)
				.split("\n");
		String[] predLines = FileUtil.getTextFromFile("el.out").split("\n");
		int correct = 0;
		int all = 0;
		for (int i = 0; i < evalLines.length; ++i) {
			String[] items = evalLines[i].split("\t");
			String[] items2 = predLines[i].split("\t");
			if (!items[0].equals(items2[0])) {
				System.out.println("wrong");
			} else {
				if (items2[1].startsWith("NIL")&& items[0].startsWith("NIL")) {
					correct++;
				} else if (!items2[1].startsWith("NIL") && items2[1].equals(items[1])) {
					correct++;
				}
			}
			all++;
		}
		System.out.println(correct+"\t"+all);
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
