package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import tackbp.KbpConstants;

/**
 *
 * @author Xiao Ling
 */

public class Configure {
	/**
	 * read a property file and set up the necessary variables.
	 * @param filename
	 */
	public static boolean setup(String filename) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(filename));
			// rootpath
			KbpConstants.rootPath = props.getProperty("rootPath");
			// dataset
			KbpConstants.processedDocPath = KbpConstants.rootPath+props.getProperty("dataset");
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
