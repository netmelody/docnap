package org.netmelody.docnap.core.type;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;

public class DocnapDateTimeTest {
	
	@Test
	public void testInstantiation() {
		assertNotNull("Docnap Date Time should be initalised", 
				  new DocnapDateTime(new Timestamp(5755757577L)));
		
	}

	@Test
	public void testGetValueAsDate() {
		long timeInMilliseconds = 575483738L;
		DocnapDateTime dateTime = new DocnapDateTime(new Timestamp(timeInMilliseconds));
		
		assertEquals("Date should be set", new Date(timeInMilliseconds), dateTime.getValueAsDate());
	}

}
