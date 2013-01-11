package el;

import util.io.FileUtil;

public class Eval {
	public static void run() {
		String[] evalLines = FileUtil.getTextFromFile(ElConstants.evalFile)
				.split("\n");
//		String[] predLines = FileUtil.getTextFromFile(
//				"data/el_predictions/el.out").split("\n");
//		String[] predLines = FileUtil.getTextFromFile("data/el_predictions/milne.pred").split("\n");
		String[] predLines = FileUtil.getTextFromFile("data/el_predictions/uiuc/el.out").split("\n");
		int correct = 0;
		int tp = 0, fp = 0, fn = 0, pos = 0;
		int all = 0;
		for (int i = 0; i < evalLines.length; ++i) {
			String[] items = evalLines[i].split("\t");
			String[] items2 = predLines[i].split("\t");
			if (!items[0].equals(items2[0])) {
				System.out.println("wrong");
			} else {
				if (items2[1].startsWith("NIL") && items[1].startsWith("NIL")) {
					correct++;
				} else if (!items2[1].startsWith("NIL")) {
					if (items2[1].equals(items[1])) {
						correct++;
						tp++;
					} else {
						if (!items[1].startsWith("NIL")) {
							pos++;
						}
						System.out.println("query = " + items2[0] + ", pred = "
								+ items2[1] + ", label = " + items[1]);
						fp++;
					}
				} else {
					if (!items[1].startsWith("NIL")) {
						fn++;
					}
					System.out.println("query = " + items2[0] + ", pred = "
							+ items2[1] + ", label = " + items[1]);
				}
			}
			all++;
		}
		System.out.println("accuracy = " + ((double) correct / all) + "\t"
				+ correct + "\t" + all);
		double prec = (double) tp / (tp + fp);
		double rec = (double) tp / (tp + fn);
		double f1 = 2 * prec * rec / (prec + rec);
		System.out.println(String.format(
				"prec=%.3f, rec=%.3f, f1=%.3f, #pos=%d", prec, rec, f1, (tp
						+ fn + pos)));
	}
}
