import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.io.FileUtil;


public class QueryReader {
	public static final String evalPath = "../tackbp_data/TAC_2011_KBP_English_Evaluation_Temporal_Slot_Filling_Queries_V1.1/data/";
	public static final String trainPath = "../tackbp_data/TAC_2011_KBP_English_Training_Temporal_Slot_Filling_Annotation/data/";
	
	public static final String queryFile = "queries.xml";
	public static final String tupleFile = "tuples.tab";
	public static final String interFile = "inter.tab";
	
	
	public void readTrainData(){
		// query
		String text = FileUtil.getTextFromFile(trainPath+queryFile);
		ArrayList<String> qids = new ArrayList<String>(), names = new ArrayList<String>(), 
		docids = new ArrayList<String>(), enttypes = new ArrayList<String>(),
		nodeids = new ArrayList<String>();
		
		Pattern p = Pattern.compile("<name>(.+?)</name>");
		Matcher m = p.matcher(text);
		while (m.find()){
			names.add(m.group(1));
		}
		
		p = Pattern.compile("<query id=\"(.+?)\">");
		m = p.matcher(text);
		while (m.find()){
			qids.add(m.group(1));
		}
		
		p = Pattern.compile("<docid>(.+?)</docid>");
		m = p.matcher(text);
		while (m.find()){
			docids.add(m.group(1));
		}
		
		p = Pattern.compile("<enttype>(.+?)</enttype>");
		m = p.matcher(text);
		while (m.find()){
			enttypes.add(m.group(1));
		}
		
		p = Pattern.compile("<nodeid>(.+?)</nodeid>");
		m = p.matcher(text);
		while (m.find()){
			nodeids.add(m.group(1));
		}
		
		
		// inter.tab
		String[] lines = FileUtil.getTextFromFile(trainPath+interFile).split("\n");
		for (int i = 0; i < lines.length; i++){
			String[] items = lines[i].split("\t");
			int idx = qids.indexOf(items[0]);
			Slot slot = new Slot();
			slot.name = names.get(idx);
			slot.rel = items[1];
			slot.val = items[4];
			slot.docid = docids.get(idx);
			Instance inst = new Instance();
			inst.slot = slot;
			if (items.length > 6){
				inst.tExp = items[6];
				inst.tRel = items[5];
			}
			else{
				inst.tRel = items[5];
			}
			System.out.println(inst);
		}
	}
}
