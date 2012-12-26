package el;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class QueryReader {
	public List<EntityMention> queryList = null;
	public void readFrom(String filename) {
		try {
			queryList = new ArrayList<EntityMention>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
//			factory.setValidating(true);
			Document doc = factory.newDocumentBuilder().parse(new File(filename));
			NodeList nodeList = doc.getElementsByTagName("query");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				EntityMention mention = new EntityMention();
				// FIXME : check if there is an attribute and if it's query id
				mention.queryId = node.getAttributes().item(0).getNodeValue();
				// FIXME : check if the child nodes are correct
				mention.mentionString = node.getChildNodes().item(1).getTextContent();
				mention.mentionDoc = node.getChildNodes().item(3).getTextContent();
//				System.out.println(mention.toString());
//				if (!new File(RetrieveDocument.getFilePath(mention.mentionDoc)).exists()) {
//					System.out.println(mention.mentionDoc+" not found");
//				}
				queryList.add(mention);
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
}
