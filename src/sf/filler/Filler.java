package sf.filler;

import java.util.List;

import sf.SFEntity;

/**
 *
 * @author Xiao Ling
 */

public interface Filler {
	public void predict(SFEntity mention, List<String> lines,
			String filename);
}
