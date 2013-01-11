package sf.retriever;

import java.util.Map;

/**
 * A fake document retriever that takes a list of 
 * file names as input. The iterator only returns 
 * this list.
 * @author xiaoling
 *
 */
public class FakeProcessedCorpus extends ProcessedCorpus {
	
	
	/**
	 * has to take meta as input 
	 * @param fileList
	 * @param dataTypes
	 * @throws Exception
	 */
	public FakeProcessedCorpus(String[] fileList, String[] dataTypes) throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean hasNext() {
		return false;
		
	}

	@Override
	public Map<String, String> next() {
		return cur;
		
	}
}
