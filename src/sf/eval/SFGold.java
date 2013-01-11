package sf.eval;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashSet;

import sf.SFConstants;
import util.FileUtil;

public class SFGold {
	public static void getGoldFromAssessment() {
		String[] files = new File(SFConstants.goldFilePrefix)
				.list(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						if (name.startsWith("SF2")) {
							return true;
						} else {
							return false;
						}
					}
				});
		StringBuilder sb = new StringBuilder();
		for (String file: files) {
			String[] lines = FileUtil.getTextFromFile(SFConstants.goldFilePrefix+file).split("\n");
			HashSet<String> visited = new HashSet<String>();
			for (String line: lines) {
				String lineWithoutId = line.substring(line.indexOf(" ")+1);
				if (line.isEmpty()|| visited.contains(lineWithoutId))
					continue;
				visited.add(lineWithoutId);
				String[] fields = line.split(" ", 6);
				if (fields.length < 2)
					System.out.println(line);
				String[] pair = fields[1].split(":", 2);
				sb.append("NA");
				sb.append("\t"+pair[0]);// eid:1
				sb.append("\t"+"NA");
				sb.append("\t"+pair[1]);// slot:3
				sb.append("\t"+fields[2]); //doc:4
				sb.append("\t"+"NA");
				sb.append("\t"+"NA");
				sb.append("\t"+"NA");
				sb.append("\t"+fields[5]); // ansString
				sb.append("\t"+fields[4]); // equiv. class
				sb.append("\t"+fields[3]);// jdg:10
				sb.append("\n");
			}
		}
		FileUtil.writeTextToFile(sb.toString(), "data/sf.gold");
	}
}
