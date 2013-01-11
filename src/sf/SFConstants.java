package sf;

import tackbp.KbpConstants;

public class SFConstants {
	public static final String queryFile = KbpConstants.rootPath
			+ "TAC_2010_KBP_Evaluation_Slot_Filling_Queries/data/tac_2010_kbp_evaluation_slot_filling_queries.xml";

	public static final String[] srcLists = {
			KbpConstants.rootPath + "TAC10Source/nw_src.list",
			KbpConstants.rootPath + "TAC10Source/wb_src.list" };

	public static final String goldFilePrefix = KbpConstants.rootPath
			+ "TAC_2010_KBP_Assessment_Results_V1.1/data/";
	
	public static final String outFile = "data/sf_predictions/sf.out";
	
	public static final String[] sources = {"09nw", /*"10wb"*/};

	public static final String[] dataTypes = {"meta","text", "tokens", "tokenSpans", "stanfordpos", "cj", "depsStanfordCCProcessed", "stanfordner"};
}
