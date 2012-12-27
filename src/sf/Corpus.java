package sf;

import util.io.FileUtil;

public class Corpus {
	 int yIdx = -1;
	 String[] years = { "2009", "2010" };
	 int dIdx = -1;
	 String[] files = null;

	public  String next() {
		if (dIdx == -1 || dIdx >= files.length-1) {
			yIdx++;
			dIdx=-1;
			if (yIdx >= years.length) {
				return null;
			}
			files = FileUtil.getTextFromFile(SFConstants.srcLists[yIdx]).split(
					"\n");
		}
		dIdx++;
		return files[dIdx];
	}
}
