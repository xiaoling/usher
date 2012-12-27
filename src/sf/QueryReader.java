package sf;

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

import tackbp.KbEntity;
import el.EntityMention;

public class QueryReader {
	public List<SFEntityMention> queryList = null;
	public void readFrom(String filename) {
		try {
			queryList = new ArrayList<SFEntityMention>();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
//			factory.setValidating(true);
			Document doc = factory.newDocumentBuilder().parse(new File(filename));
			NodeList nodeList = doc.getElementsByTagName("query");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				SFEntityMention mention = new SFEntityMention();
				// FIXME : check if there is an attribute and if it's query id
				mention.queryId = node.getAttributes().item(0).getNodeValue();
				NodeList children = node.getChildNodes();
				for (int j = 0; j < children.getLength(); ++j) {
					if (children.item(j).getNodeName().equals("name")) {
						mention.mentionString = children.item(j).getTextContent();
					} else if (children.item(j).getNodeName().equals("docid")) {
						mention.mentionDoc = children.item(j).getTextContent();
					} else if (children.item(j).getNodeName().equals("enttype")) {
						mention.entityType = KbEntity.EntityType.valueOf(children.item(j).getTextContent());
					} else if (children.item(j).getNodeName().equals("nodeid")) {
						mention.entityId = children.item(j).getTextContent();
					} else if (children.item(j).getNodeName().equals("ignore")) {
						for (String slot: children.item(j).getTextContent().split(" ")) {
							mention.ignoredSlots.add(slot);
						}
					}
				}
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
