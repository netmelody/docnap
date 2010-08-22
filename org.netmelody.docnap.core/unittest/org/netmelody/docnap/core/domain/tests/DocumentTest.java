package org.netmelody.docnap.core.domain.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Timestamp;

import org.junit.Test;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.type.DocnapDateTime;

public class DocumentTest {
    
    private final Document defaultDocument = createDocument();
    
	@Test
	public void testInstantiationNull() {
		final Document document = new Document();
		
		assertNotNull("New document should not be null", document);
		assertNull("Title of new document should be null", document.getTitle());
		assertNull("Date Added of new document should be null", document.getDateAdded());
		assertNull("Basic Date Added of new document should be null", document.getDateAdded());
		assertNull("Original Filename of new document should be null", document.getOriginalFilename());
		assertNull("Identity of new document should be null", document.getIdentity());
	}
	
	@Test
	public void testInstantiation() {
		final Document document = new Document(5, "handle5");
		
		assertNotNull("New document should not be null", document);
		assertNull("Title of new document should be null", document.getTitle());
		assertNull("Date Added of new document should be null", document.getDateAdded());
		assertNull("Original Filename of new document should be null", document.getOriginalFilename());
		assertEquals("Identity of new document should be set", new Integer(5), document.getIdentity());
	}
	
	private Document createDocument(Integer identity, String handle) {
		return new Document(identity, handle);
	}
	
	private Document createDocument() {
		return createDocument(5, "jkl");
	}

	@Test
	public void testTitle() {
		final String newTitle = "NewTitle";
		final Document document = createDocument();
		
		document.setTitle(newTitle);
		assertEquals("Title should be set", newTitle, document.getTitle());
	}

	@Test
	public void testDateAdded() {
		final DocnapDateTime dateAdded = new DocnapDateTime(new Timestamp(4638458));
		final Document document = createDocument();

		
		document.setDateAdded(dateAdded);
		assertEquals("Date added should be set", dateAdded, document.getDateAdded());
	}

	@Test
	public void testSetOriginalFilename() {
    	final String originalFilename = "Original FileName.lst";
		final Document document = createDocument();
			
		document.setOriginalFilename(originalFilename);
		assertEquals("Original filename should be set", originalFilename, document.getOriginalFilename());
	}

	@Test
	public void testToStringNoTitle() {
		final Integer identity = new Integer(7);
		final Document document = createDocument(identity, "thisisateapot");
		
		assertEquals("toString should return the identity", identity.toString(), document.toString());
	}
	
	@Test
	public void testToStringWithTitle() {
		final Document document = createDocument(new Integer(6), "thisisateapot");
		
		final String title = "The dragon and the rock";
		document.setTitle(title);
		
		assertEquals("toString should return the title", title, document.toString());
	}

	@Test
	public void testGetIdentity() {
		final Integer identity = new Integer(52);
		final Document document = createDocument(identity, "thisisateapot");
		
		assertEquals("Identity should be same as set", identity, document.getIdentity());
	}
	
	@Test
	public void testEqualsTwoAreSameDocument() {
	    assertTrue("Documents should be equal", defaultDocument.equals(defaultDocument));
	}
	
	@Test
	public void testEqualsSecondNotDocument() {
	    assertFalse("Objects should not be equal", defaultDocument.equals(new Integer(5)));
	}
	
	@Test
    public void testEqualsTwoNotSameIdentity() {
        assertFalse("Objects should not be equal", defaultDocument.equals(createDocument(34, "Doc")));
    }
	
	@Test
    public void testEqualsTwoDocumentSameIdentityNotSameObject() {
        assertTrue("Documents should be equal", defaultDocument.equals(createDocument()));
    }
	
	@Test
	public void testHashcode() {
	    assertEquals("Hashcodes should be the same", createDocument().hashCode(), defaultDocument.hashCode());
	    assertTrue("Hashcodes should not be the same", createDocument(36, "fhf").hashCode() != defaultDocument.hashCode());
	}

}
