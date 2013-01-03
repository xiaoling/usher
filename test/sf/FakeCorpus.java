package sf;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Xiao Ling
 */

public class FakeCorpus extends Corpus{

	public List<String> fileList = new ArrayList<String>();
	int idx = -1;
	public FakeCorpus(String[] files) {
		for (String str: files) {
			fileList.add(str);
		}
	}
	@Override
	public String next() {
		idx++;
		if (idx >= fileList.size()) {
			return null;
		} else {
			return fileList.get(idx);
		}
	}
}