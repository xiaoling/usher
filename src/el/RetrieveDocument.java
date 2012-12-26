package el;


public class RetrieveDocument {
	public static String getFilePath(String queryDoc) {
		if (queryDoc.endsWith("LDC2009T13") || queryDoc.endsWith("LDC2007T07")) {
			// 09 data
			String[] pieces = queryDoc.split("_|\\.");
			String file = pieces[0].toLowerCase() + "_"
					+ pieces[1].toLowerCase() + "/" + pieces[2] + "/" + queryDoc
					+ ".sgm";
			return ElContants.docSourcePath + "2009/nw/" + file;
		} else {
			// 10 data
			return ElContants.docSourcePath + "2010/wb/" + queryDoc + ".sgm";
		}
	}
}
