package tsf;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.io.FileUtil;


public class QueryReader {
	public static final String evalPath = "../tackbp_data/TAC_2011_KBP_English_Evaluation_Temporal_Slot_Filling_Queries_V1.1/data/";
	public static final String trainPath = "../tackbp_data/TAC_2011_KBP_English_Training_Temporal_Slot_Filling_Annotation/data/";
	public static final String srcPath ="../tackbp_data/TAC10Source/TAC_2010_KBP_Source_Data/data/2009/nw/";
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
//			slot.docid = docids.get(idx);
			slot.docid = items[3];
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

			// extract the sentences
			//AFP_ENG_20071230.5007.LDC2009T13
			String[] pieces = inst.slot.docid.split("_|\\.");
			String file = srcPath + pieces[0].toLowerCase() +"_"+pieces[1].toLowerCase()+"/"+pieces[2]+"/"+inst.slot.docid+".sgm";
			if (items.length > 6){
				if (new File(file).exists()){
					//items[7,8]
					int start = Integer.parseInt(items[7]), end = Integer.parseInt(items[8]);
					Pattern para = Pattern.compile("<P>(.+?)</P>", Pattern.DOTALL);
					String srcText = FileUtil.getTextFromFile(file);
					Matcher mPara = para.matcher(srcText);
					boolean found = false;
					while (mPara.find()){
						if (mPara.start()<= start && mPara.end() >= end){
							if (!srcText.substring(start, end).replace("\n", " ").equals(items[6])){
								System.err.println("text not match ::"+items[6]+"::" + file+"@@"+inst.slot.docid);
							}
							System.out.println("TEXT:"+srcText.substring(start, end));
							System.out.println(mPara.group(1));
							found = true;
							break;
						}
					}
					if (!found){
						System.err.println("text not find ::" + file+"@@"+inst.slot.docid);
					}
				}
				else{
					System.err.println("can't find ::" + file+"@@"+inst.slot.docid);
				}
			}
			else{
				System.out.println("currently...");
			}
		}
	}

	public static void main(String[] args){
		QueryReader qr = new QueryReader();
		qr.readTrainData();
	}
}
	