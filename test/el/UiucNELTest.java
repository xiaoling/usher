package el;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author Xiao Ling
 */

public class UiucNELTest {

	@Test
	public void test() {
		String entity = UiucNEL
				.getSegmentWikification(
						"data/el_predictions/uiuc/wikified/1030.wikification.tagged.full.xml",
						37, 45);
		assertEquals(entity, "Virginia");
		entity = UiucNEL
				.getSegmentWikification(
						"data/el_predictions/uiuc/wikified/1030.wikification.tagged.full.xml",
						329, 333);
		assertEquals(entity, "2005");
	}

}
