package sf.retriever;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A fake document retriever that takes a list of file names as input. The
 * iterator only returns this list.
 * 
 * @author xiaoling
 * 
 */
public class FakeProcessedCorpus extends ProcessedCorpus {

	Set<String> filter = null;

	/**
	 * has to take meta as input
	 * 
	 * @param fileList
	 *            a list of files
	 * @param dataTypes
	 * @throws Exception
	 */
	public FakeProcessedCorpus(String[] fileList, String[] dataTypes)
			throws Exception {
		super();
		filter = new HashSet<String>();
		for (String file: fileList) {
			filter.add(file);
		}
	}

	@Override
	public boolean hasNext() {
		// TODO
		return false;
	}

	@Override
	public Map<String, String> next() {
		// TODO
		return cur;
	}
}
