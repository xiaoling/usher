import java.io.IOException;
import java.util.List;
import java.util.Map;

import sf.SFConstants;
import sf.SFEntity;
import sf.SFEntity.SingleAnswer;
import sf.eval.SFScore;
import sf.filler.Filler;
import sf.filler.RegexBirthdateFiller;
import sf.retriever.ProcessedCorpus;
import tackbp.RetrieveDocument;
import util.FileUtil;

/**
 * CSE 454 Assignment 1 main class. Java 7 required.
 * 
 * In the main method, a pipeline is run as follows:
 * 1) Read the queries.
 * 2) For each query, retrieve relevant documents.
 * In this assignment, only the documents containing
 * an answer will be returned to save running time.
 * In practice, a search-engine-style retriever will
 * be used. Iterate through all the sentences
 * returned and the slot filler will applied to
 * extract answers.
 * 3) Spit out answers and evaluate them against the
 * labels.
 * 
 * In this assignment, you only need to write a new
 * class for the assigned slots implementing the <code>sf.filler.Filler</code>
 * interface. An example
 * class on birthdate is implemented in
 * <code>sf.filler.RegexBirthdateFiller.java</code>.
 * 
 * @author Xiao Ling
 */

public class Assignment1 {
	public static void main(String[] args) {
		// if the first 2 steps are run
		boolean run = true;
		// if the 3rd step is run
		boolean eval = true;
		if (args.length == 1) {
			if (args[1].equals("run")) {
				run = true;
				eval = false;
			} else if (args[1].equals("eval")) {
				run = false;
				eval = true;
			}
		}

		// The slot filling pipeline
		if (run) {
			// read the queries
			sf.query.QueryReader queryReader = new sf.query.QueryReader();
			queryReader.readFrom(SFConstants.queryFile);

			// initialize the filler
			Filler filler = new RegexBirthdateFiller();

			StringBuilder answersString = new StringBuilder();
			for (SFEntity query : queryReader.queryList) {
				// initialize the corpus
				// FIXME replace the list by a generic class with an input of slot
				// name and an output of all the relevant files from the answer file
				ProcessedCorpus corpus;
				try {
					corpus = new ProcessedCorpus();
					Map<String, String> annotations = null;
					int c = 0;
					while (corpus.hasNext()) {
						annotations = corpus.next();
						if (c++ % 1000 == 0) {
							System.out.print("finished reading " + c + " lines\r");
						}
						// apply the filler to the sentences with its annotations
						filler.predict(query, annotations);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				// Print out the answer
				if (query.answers.containsKey(RegexBirthdateFiller.slotName)) {
					// The output file format
					// Column 1: query id
					// Column 2: the slot name
					// Column 3: a unique run id for the submission
					// Column 4: NIL, if the system believes no information is
					// learnable for this slot. Or, a single docid
					// which supports the slot value
					// Column 5: a slot value
					SingleAnswer ans = (SingleAnswer) query.answers
							.get(RegexBirthdateFiller.slotName);
					answersString.append(String.format("%s\t%s\t%s\t%s\t%s\n",
							query.queryId, RegexBirthdateFiller.slotName,
							filler.getClass().getName(), ans.doc, ans.answer));
				} else {
					answersString.append(String.format("%s\t%s\t%s\t%s\t%s\n",
							query.queryId, RegexBirthdateFiller.slotName,
							filler.getClass().getName(), "NIL", ""));
				}
			}
			FileUtil.writeTextToFile(answersString.toString(),
					SFConstants.outFile);
		}
		if (eval) {
			// Evaluate against the gold standard labels
			try {
				// SFGold.getGoldFromAssessment();
				SFScore.main(new String[] { SFConstants.outFile,
						"data/sf.gold" });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
