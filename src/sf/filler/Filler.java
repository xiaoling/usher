package sf.filler;

import java.util.Map;

import sf.SFEntity;

/**
 *
 * @author Xiao Ling
 */

public interface Filler {
	public void predict(SFEntity mention, Map<String, String> annotations);
}
