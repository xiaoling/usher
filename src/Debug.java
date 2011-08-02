import util.io.FileUtil;


public class Debug {
	public static void main(String[] args){
		String doc = "AFP_ENG_20070129.0462.LDC2009T13";
		String[] pieces = doc.split("_|\\.");
		String file = pieces[0].toLowerCase() +"_"+pieces[1].toLowerCase()+"/"+pieces[2]+"/"+doc+".sgm";
		String text = FileUtil.getTextFromFile("y:/data12/xiaoling/tackbp_temporal11/tackbp_data/TAC10Source/TAC_2010_KBP_Source_Data/data/2009/nw/"+file);
		String arg1 = "pianist";
		String arg2 = "Sutil";
		
		String[] lines = text.split("\n");
		System.out.println(text);
		System.out.println("(*************)");
		
		for (String line: lines){
			if (line.contains(arg1) || line.contains(arg2)){
				System.out.println(line);
			}
		}
	}
}
