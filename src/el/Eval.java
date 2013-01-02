package el;

import util.io.FileUtil;

public class Eval {
	public static void run() {
		String[] evalLines = FileUtil.getTextFromFile(ElConstants.evalFile)
				.split("\n");
		String[] predLines = FileUtil.getTextFromFile("data/el_predictions/el.out").split("\n");
		int correct = 0;
		int all = 0;
		for (int i = 0; i < evalLines.length; ++i) {
			String[] items = evalLines[i].split("\t");
			String[] items2 = predLines[i].split("\t");
			if (!items[0].equals(items2[0])) {
				System.out.println("wrong");
			} else {
				if (items2[1].startsWith("NIL") && items[1].startsWith("NIL")) {
					correct++;
				} else if (!items2[1].startsWith("NIL")
						&& items2[1].equals(items[1])) {
					correct++;
				} else {
					System.out.println("query = "+items2[0]+", pred = "+items2[1] +", label = "+items[1]);
				}
			}
			all++;
		}
		System.out.println("accuracy = " + ((double) correct / all) + "\t"
				+ correct + "\t" + all);
	}
}
