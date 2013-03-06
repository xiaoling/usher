package el;


import java.util.logging.Logger;

import tackbp.KB;
import util.FileUtil;

/**
 * 
 * @author Xiao Ling
 */

public class Main {
	private static final Logger logger = Logger.getLogger(Main.class.getName());
	public static void main(String[] args) {
		boolean run = false;
		boolean eval = false;
		if (args.length == 1) {
			if (args[0].equals("all")) {
				run = true;
				eval = true;
			} else if (args[0].equals("run")) {
				run = true;
			} else if (args[0].equals("eval")) {
				eval = true;
			}
		}
		if (run) {
			logger.fine("Running...");
			KB kb = new KB();
			kb.init();
			
			el.QueryReader qReader = new el.QueryReader();
			qReader.readFrom(ElConstants.queryFile);
			// StringMatchBaseline baseline = new StringMatchBaseline();
//			MilneNEL baseline = new MilneNEL();
			OpenIeNEL baseline = new OpenIeNEL();
			for (EntityMention mention : qReader.queryList) {
				baseline.predict(mention, kb);
			}
			StringBuilder sb = new StringBuilder();
			for (EntityMention mention : qReader.queryList) {
				sb.append(mention.queryId + "\t" + mention.entityId + "\n");
			}
			FileUtil.writeTextToFile(sb.toString(),
					"data/el_predictions/openie/el.out");
		}
		if (eval) {
			logger.fine("Eval...");
			Eval.run();
		}
	}
}
