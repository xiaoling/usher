package el;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElContants {
	public static String rootPath = null;
	static {
		try {
			if (InetAddress.getLocalHost().getCanonicalHostName()
					.endsWith("cs.washington.edu")) {
				rootPath = "/homes/gws/xiaoling/dataset/TACKBP/";
			} else {
				rootPath = "data/";
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	// public static final String rootPath = "data/";
	public static final String queryFile = rootPath
			+ "TAC_2010_KBP_Evaluation_Entity_Linking_Queries/data/tac_2010_kbp_evaluation_entity_linking_queries.xml";
	// 2009, 2010 and their subfolders
	public static final String docSourcePath = rootPath
			+ "TAC10Source/TAC_2010_KBP_Source_Data/data/";

	public static final String kbPath = rootPath + "TAC09KBPEvalRefKB/data/";
	public static final int kbFileStart = 1;
	public static final int kbFileEnd = 89; // exclusive
	public static final String kbFileFormat = "kb_part-%04d.xml";

	public static final String evalFile = rootPath
			+ "TAC_2010_KBP_Evaluation_Entity_Linking_Gold_Standard_V1.0/TAC_2010_KBP_Evaluation_Entity_Linking_Gold_Standard_V1.1/data/tac_2010_kbp_evaluation_entity_linking_query_types.tab";
}
