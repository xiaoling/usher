package sf;

import tackbp.KbpConstants;

public class SFConstants {
	public static final String STANFORDNER = "stanfordner";

	public static final String DEPS_STANFORD_CC_PROCESSED = "depsStanfordCCProcessed";

	public static final String CJ = "cj";

	public static final String COREF = "coref";

	public static final String STANFORDPOS = "stanfordpos";

	public static final String TOKEN_SPANS = "tokenSpans";

	public static final String TOKENS = "tokens";

	public static final String TEXT = "text";

	public static final String META = "meta";

	public static final String WIKIFICATION = "wikification";

	public static final String queryFile = KbpConstants.rootPath
			+ "TAC_2010_KBP_Evaluation_Slot_Filling_Queries/data/tac_2010_kbp_evaluation_slot_filling_queries.xml";

	public static final String[] srcLists = {
			KbpConstants.rootPath + "TAC10Source/nw_src.list",
			KbpConstants.rootPath + "TAC10Source/wb_src.list" };

	public static final String goldFilePrefix = KbpConstants.rootPath
			+ "TAC_2010_KBP_Assessment_Results_V1.1/data/";

	public static final String outFile = "data/sf.pred";
	public static final String labelFile = "data/sf.gold";
	// TODO allows multiple sources or concat them together?
	// public static final String[] sources = {"09nw", /*"10wb"*/};

	public static final String prefix = "sentences";

	public static final String[] dataTypes = { META, TEXT, TOKENS, TOKEN_SPANS,
			STANFORDPOS, CJ, DEPS_STANFORD_CC_PROCESSED, STANFORDNER,/* COREF,*/
			/*WIKIFICATION*/ };
}
