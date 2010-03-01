package org.netmelody.docnap.core.domain.tests;

import org.junit.Test;
import org.netmelody.docnap.core.domain.DocnapObject;

import static org.junit.Assert.assertNotNull;

public class DocnapObjectTest {
	
	@Test
	public void testInstantiation() {
		DocnapObject newDocnapObject = new DocnapObject();
		
		assertNotNull("New Object should not be null", newDocnapObject);
	}

}
