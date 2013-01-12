package sf.retriever;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sf.SFConstants;

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
		if (!dataReaders.containsKey(SFConstants.META)) {
			throw new Exception("meta data is not included in the corpus.");
		}
		filter = new HashSet<String>();
		for (String file : fileList) {
			filter.add(file);
		}
	}

	@Override
	public boolean hasNext() {
		if (cache != null) {
			return true;
		} else {
			try {
				cache = dataReaders.get(SFConstants.META).readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (cache == null) {
				cur = null;
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	public Map<String, String> next() {
		if (cur == null) {
			return cur;
		}
		if (cache != null) {
			cur.put(SFConstants.META, cache);
			cache = null;
		} else {
			try {
				cur.put(SFConstants.META, dataReaders.get(SFConstants.META).readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < dataTypes.length; ++i) {
			if (dataTypes[i].equals(SFConstants.META)) {
				continue;
			}
			try {
				cur.put(dataTypes[i], dataReaders.get(dataTypes[i]).readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return cur;
	}
}
