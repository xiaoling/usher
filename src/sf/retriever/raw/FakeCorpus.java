package sf.retriever.raw;

import java.util.ArrayList;
import java.util.List;

/**
 * A fake document retriever that takes a list of 
 * file names as input. The iterator only returns 
 * this list.
 * @author Xiao Ling
 */

public class FakeCorpus extends Corpus {

	public List<String> fileList = new ArrayList<String>();
	int idx = -1;

	public FakeCorpus(String[] files) {
		for (String str : files) {
			fileList.add(str);
		}
	}

	@Override
	public String next() {
		idx++;
		if (idx < 0 || idx >= fileList.size()) {
			return null;
		} else {
			return fileList.get(idx);
		}
	}
	
	@Override
	public boolean hasNext() {
		if (idx >= 0 && idx < fileList.size() - 1) {
			return true;
		} else {
			return false;
		}
	}
}