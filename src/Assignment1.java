import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sf.SFConstants;
import sf.SFEntity;
import sf.SFEntity.SingleAnswer;
import sf.eval.SFScore;
import sf.filler.Filler;
import sf.retriever.ProcessedCorpus;
import util.FileUtil;

/**
 * CSE 454 Assignment 1 main class. Tests passed under Java 7.
 * 
 * In the main method, a pipeline is run as follows: 1) Read the queries. 2) For
 * each query, retrieve relevant documents. In this assignment, only the
 * documents containing an answer will be returned to save running time. In
 * practice, a search-engine-style retriever will be used. Iterate through all
 * the sentences returned and the slot filler will applied to extract answers.
 * 3) Spit out answers and evaluate them against the labels.
 * 
 * In this assignment, you only need to write a new class for the assigned slots
 * implementing the <code>sf.filler.Filler</code> interface. An example class on
 * birthdate is implemented in
 * <code>sf.filler.RegexPerDateOfBirthFiller.java</code>.
 * 
 * @author Xiao Ling
 */

public class Assignment1 {
	public static void main(String[] args) throws Exception {
		// if the first 2 steps are run
		boolean run = true;
		// if the 3rd step is run
		boolean eval = true;
		if (args.length == 1) {
			if (args[0].equals("run")) {
				run = true;
				eval = false;
			} else if (args[0].equals("eval")) {
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
			List<Filler> fillers = new ArrayList<Filler>();
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.OrgCountryOfHeadquartersFiller")
					.newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.OrgDissolvedFiller").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.OrgFoundedFiller").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.OrgNumberOfEmployeesFiller").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.OrgStateOrProvinceOfHeadquartersFiller")
					.newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.OrgWebsiteFiller").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.PerCauseOfDeathFiller").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.PerCountryOfBirthFiller2").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.PerCountryOfBirthFiller").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.PerDateOfDeathFiller2").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.PerDateOfDeathFiller").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.PerStateorprovinceOfDeathFiller")
					.newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.RegexOrgCountryOfHeadquartersFiller2")
					.newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.RegexOrgCountryOfHeadquartersFiller")
					.newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.RegexPerAgeFiller").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.RegexPerCityOfBirthFiller").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.RegexPerCountryOfBirthFiller")
					.newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.RegexPerCountryOfBirth").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.RegexPerCountryOfDeathFiller")
					.newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.RegexPerDateOfBirthFiller").newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.RegexPerStateorprovinceOfBirthFiller")
					.newInstance());
			fillers.add((Filler) Class.forName(
					"sf.filler.regex.RegexPerStateOrProvinceOfDeathFiller")
					.newInstance());
			// Filler filler = new RegexPerDateOfBirthFiller();

			StringBuilder answersString = new StringBuilder();
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
					if (c++ % 100000 == 0) {
						System.err.print("finished reading " + c + " lines\r");
					}

					// for each query, find out if the slot can be filled
					for (SFEntity query : queryReader.queryList) {
						// apply the filler to the sentences with its
						// annotations
						for (Filler filler : fillers) {
							filler.predict(query, annotations);
						}
					}
				}
				// for each query, print it out
				for (SFEntity query : queryReader.queryList) {
					// Print out the answer
					for (Filler filler : fillers) {
						if (query.answers.containsKey(filler.slotName)) {
							// The output file format
							// Column 1: query id
							// Column 2: the slot name
							// Column 3: a unique run id for the submission
							// Column 4: NIL, if the system believes no
							// information is learnable for this slot. Or,
							// a single docid which supports the slot value
							// Column 5: a slot value
							SingleAnswer ans = (SingleAnswer) query.answers
									.get(filler.slotName);
							answersString.append(String.format(
									"%s\t%s\t%s\t%s\t%s\n", query.queryId,
									filler.slotName, filler.getClass()
											.getName(), ans.doc, ans.answer));
						} else {
							answersString.append(String.format(
									"%s\t%s\t%s\t%s\t%s\n", query.queryId,
									filler.slotName, filler.getClass()
											.getName(), "NIL", ""));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("writing answers");
			FileUtil.writeTextToFile(answersString.toString(),
					SFConstants.outFile);
		}
		if (eval) {
			// Evaluate against the gold standard labels
			// The label file format (11 fields):
			// 1. NA
			// 2. query id
			// 3. NA
			// 4. slot name
			// 5. from which doc
			// 6., 7., 8. NA
			// 9. answer string
			// 10. equiv. class for the answer in different strings
			// 11. judgement. Correct ones are labeled as 1.
			System.out.println(SFConstants.outFile);
			try {
				SFScore.main(new String[] { SFConstants.outFile,
						SFConstants.labelFile/*,  "anydoc", "nocase",
						"slots=data/sf.allslots"*/ });
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
