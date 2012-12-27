package tackbp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class RetrieveDocument {
	public static String getFilePath(String queryDoc) {
		if (queryDoc.endsWith("LDC2009T13") || queryDoc.endsWith("LDC2007T07")) {
			// 09 data
			String[] pieces = queryDoc.split("_|\\.");
			String file = pieces[0].toLowerCase() + "_"
					+ pieces[1].toLowerCase() + "/" + pieces[2] + "/"
					+ queryDoc + ".sgm";
			return KbpConstants.docSourcePath + "2009/nw/" + file;
		} else if (queryDoc.endsWith("LDC98T25")) {
			// 09 data
			String[] pieces = queryDoc.split("_|\\.");
			return KbpConstants.docSourcePath + "2009/nw/reu_eng/19"
					+ pieces[1] + "/" + queryDoc + ".sgm";
		} else {
			// 10 data
			return KbpConstants.docSourcePath + "2010/wb/" + queryDoc + ".sgm";
		}
	}

	public static List<String> getContent(String filename) {
		List<String> lines = new ArrayList<String>();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			// factory.setValidating(true);
			Document doc = factory.newDocumentBuilder().parse(
					new File(getFilePath(filename)));
			if (filename.contains("2009/nw")) {
				NodeList nodeList = doc.getElementsByTagName("P");
				for (int i = 0; i < nodeList.getLength(); i++) {
					lines.add(nodeList.item(i).getTextContent());
				}
			} else if (filename.contains("2010/wb")) {
				NodeList nodeList = doc.getElementsByTagName("POST");
				if (nodeList.getLength() > 1) {
					System.err.println("> 1 POST nodes!" + filename);
				}
				nodeList = nodeList.item(0).getChildNodes();
				for (int i = nodeList.getLength() - 1; i > -1; i--) {
					if (nodeList.item(i).getNodeName().equals("#text")) {
						for (String line : nodeList.item(i).getTextContent()
								.split("\n\n")) {
							lines.add(line);
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lines;
	}
}
