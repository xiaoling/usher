package el;

import tackbp.KbpConstants;

public class ElConstants {
	// public static final String rootPath = "data/";
	public static final String queryFile = KbpConstants.rootPath
			+ "TAC_2010_KBP_Evaluation_Entity_Linking_Queries/data/tac_2010_kbp_evaluation_entity_linking_queries.xml";
	public static final String kbPath = KbpConstants.rootPath + "TAC09KBPEvalRefKB/data/";
	public static final int kbFileStart = 1;
	public static final int kbFileEnd = 89; // exclusive
	public static final String kbFileFormat = "kb_part-%04d.xml";

	public static final String evalFile = KbpConstants.rootPath
			+ "TAC_2010_KBP_Evaluation_Entity_Linking_Gold_Standard_V1.0/TAC_2010_KBP_Evaluation_Entity_Linking_Gold_Standard_V1.1/data/tac_2010_kbp_evaluation_entity_linking_query_types.tab";
}
