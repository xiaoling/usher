package tackbp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class KbpConstants {
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
	// 2009, 2010 and their subfolders
	public static final String docSourcePath = rootPath
			+ "TAC10Source/TAC_2010_KBP_Source_Data/data/";
	
	public static final String processedDocPath = "/projects/pardosa/s5/raphaelh/tac/data/09nw/";
}
