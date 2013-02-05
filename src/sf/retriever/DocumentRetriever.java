package sf.retriever;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sf.SFConstants;
import tackbp.KbpConstants;

/**
 * An iterator of sentences and their annotations from the corpus. Do not forget
 * to call close() after use.
 * 
 * @author xiaoling
 * 
 */
public class DocumentRetriever implements Iterator<Document> {
	public DocumentRetriever() throws Exception {
		dataTypes = SFConstants.dataTypes;
		init();
	}

	public DocumentRetriever(String[] dts) throws Exception {
		dataTypes = dts;
		init();
	}

	protected String[] dataTypes = null;

	protected Map<String, BufferedReader> dataReaders = null;

	protected Document cur = null;

	// caching the meta line
	protected String metaCache = null;

	// cache the coref line
	protected String corefCache = null;

	boolean filled = false;

	public void init() throws Exception {
		dataReaders = new HashMap<String, BufferedReader>();
		cur = new Document();
		if (dataTypes.length == 0) {
			throw new Exception("No data type is specified");
		}
		boolean hasMeta = false;
		boolean hasCoref = false;
		for (String dataType : dataTypes) {
			if (dataType.equals(SFConstants.META)) {
				hasMeta = true;
			}
			if (dataType.equals(SFConstants.COREF)) {
				hasCoref = true;
			}
		}
		if (!hasMeta || !hasCoref) {
			throw new Exception("meta needs to be one of the data types");
		}
		for (String dataType : dataTypes) {
			try {
				String filename = KbpConstants.processedDocPath
						+ SFConstants.prefix + "." + dataType;
				if (new File(filename).exists()) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									new FileInputStream(filename), "UTF-8"));
					dataReaders.put(dataType, reader);
				} else {
					throw new Exception("The data type [" + dataType
							+ "] does not exist.");
				}
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		// init cache
		metaCache = dataReaders.get(SFConstants.META).readLine();
	}

	public void close() {
		for (BufferedReader reader : dataReaders.values()) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * read the next line from dataTypes[0], cache it for later use.
	 */
	@Override
	public boolean hasNext() {
		if (filled) {
			return true;
		} else {
			cur = next();
			if (cur == null) {
				return false;
			} else {
				filled = true;
				return true;
			}
		}
	}

	@Override
	public Document next() {
		if (metaCache == null) {
			filled = false;
			return null;
		}

		// use meta data to determine the number of sentences in a document
		String doc = metaCache.split("\t")[2];
		cur = new Document();
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put(SFConstants.META, metaCache);
			cur.sentAnnotations.add(map);
		}
		while (true) {
			try {
				metaCache = dataReaders.get(SFConstants.META).readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (metaCache == null || !metaCache.split("\t")[2].equals(doc)) {
				break;
			} else {
				Map<String, String> map = new HashMap<String, String>();
				map.put(SFConstants.META, metaCache);
				cur.sentAnnotations.add(map);
			}
		}
		// fill in the rest of information
		int numSent = cur.sentAnnotations.size();
		for (int i = 0; i < numSent; ++i) {
			try {
				for (String dt : dataTypes) {
					if (dt.equals(SFConstants.META)
							|| dt.equals(SFConstants.COREF)) {
						continue;
					}
					cur.sentAnnotations.get(i).put(dt,
							dataReaders.get(dt).readLine());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// fill in the coref
		try {
			if (corefCache == null) {
				corefCache = dataReaders.get(SFConstants.COREF).readLine();
			}
			while (true) {
				if (corefCache.split("\t")[0].equals(doc)) {
					break;
				}
				CorefMention mention = new CorefMention(corefCache);
				cur.addCorefMention(mention);
				corefCache = dataReaders.get(SFConstants.COREF).readLine();
				if (corefCache == null){
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO link nel and coref
		
		filled = false;

		return cur;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}