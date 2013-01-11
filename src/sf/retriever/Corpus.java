package sf.retriever;

import java.util.Iterator;

import sf.SFConstants;
import util.io.FileUtil;

/**
 * An iterator of the filenames from the corpus.
 * 
 * The real file path should be translated by the method
 * <code>tackbp.RetrieveDocument.getFilePath</code>.
 * 
 * @author xiaoling
 * 				
 */
public class Corpus implements Iterator<String> {
	int yIdx = -1;
	String[] years = { "2009", "2010" };
	int dIdx = -1;
	String[] files = null;

	/**
	 * FIXME false only if the srcList is empty
	 */
	@Override
	public String next() {
		if (dIdx == -1 || dIdx >= files.length - 1) {
			yIdx++;
			dIdx = -1;
			if (yIdx >= years.length) {
				// skip
			} else {
				files = FileUtil.getTextFromFile(SFConstants.srcLists[yIdx])
						.split("\n");
			}
		}
		dIdx++;
		if (dIdx > -1 && dIdx < files.length)
			return files[dIdx];
		else
			return null;
	}

	/**
	 * FIXME false only if the srcList is empty
	 */
	@Override
	public boolean hasNext() {
		if (dIdx == -1 || dIdx >= files.length - 1) {
			if (yIdx+1 < 0 || yIdx+1 >= years.length) {
				return false;
			} else {
 
				return true;
			}
		}
		return true;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
