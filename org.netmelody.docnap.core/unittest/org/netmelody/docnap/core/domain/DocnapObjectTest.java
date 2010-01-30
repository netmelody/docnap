package org.netmelody.docnap.core.domain;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DocnapObjectTest {
	
	@Test
	public void testInstantiation() {
		DocnapObject newDocnapObject = new DocnapObject();
		
		assertNotNull("New Object should not be null", newDocnapObject);
	}

}
