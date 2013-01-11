package sf.retriever;

import java.util.Iterator;

/**
 * An iterator of the filenames from the corpus.
 * 
 * The real file path should be translated by the method
 * <code>tackbp.RetrieveDocument.getFilePath</code>.
 * 
 * @author xiaoling
 * 
 */
public class ProcessedCorpus implements Iterator<String> {

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}