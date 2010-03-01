package org.netmelody.docnap.core.domain.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.type.DocnapDateTime;
import java.sql.Timestamp;

public class TagTest {
    
    private final Tag defaultTag = createNewTag();
	
	@Test
	public void testInstantiation() {
		final Tag newTag = new Tag(1);
		
		assertNotNull("New Tag Object should not be null", newTag);
		assertNotNull("Identity should not be null for new tag", newTag.getIdentity());
		assertEquals("Identity should be set for new Tag", new Integer(1), newTag.getIdentity());
		assertNull("Title for new tag should be null", newTag.getTitle());
		assertNull("Description for new tag should be null", newTag.getDescription());
		assertNull("Date Created for new tag should be null", newTag.getDateCreated());
		assertEquals("Document count should be 0 for new tag", 0, newTag.getDocumentCount());
	}
	
	private Tag createNewTag() {
		return createNewTag(1);
	}
	
	private Tag createNewTag(int identity) {
		return new Tag(identity);
	}

	@Test
	public void testTitle() {
		final String NEW_TITLE = "This is a new title";
		final Tag tag = createNewTag();
		
		assertNull("Title should be null to start", tag.getTitle());
		tag.setTitle(NEW_TITLE);
		assertEquals("Title should be set", NEW_TITLE, tag.getTitle());
	}



	@Test
	public void testDescription() {
		final String NEW_DESCRIPTION = "This is a new description";
		final Tag tag = createNewTag();
		
		assertNull("Description should be null to start", tag.getDescription());
		tag.setDescription(NEW_DESCRIPTION);
		assertEquals("Description should be set", NEW_DESCRIPTION, tag.getDescription());
	}

	@Test
	public void testGetIdentity() {
		final int IDENTITY = 5;
		final Tag tag = createNewTag(IDENTITY);
		
		assertEquals("Identity should be 5", new Integer(IDENTITY), tag.getIdentity());
	}

	@Test
	public void testDateCreated() {
		final DocnapDateTime dateCreated = new DocnapDateTime(new Timestamp(12345678));
		final Tag tag = createNewTag();
		
		assertNull("Date Created should be null to start", tag.getDateCreated());
		tag.setDateCreated(dateCreated);
		assertEquals("Date Created should be set", dateCreated, tag.getDateCreated());
	}
	
	@Test
	public void testDocumentCount() {
		final int DOCUMENT_COUNT = 7;
		final Tag tag = createNewTag();
		
		assertEquals("Document count should be 0", 0, tag.getDocumentCount());
		tag.setDocumentCount(DOCUMENT_COUNT);
		assertEquals("Document count should be set", DOCUMENT_COUNT, tag.getDocumentCount());
	}



	@Test
	public void testToString() {
		final String NEW_TITLE = "This is a title for the to string";
		final Tag tag = createNewTag();
		
		assertEquals("To string method should return empty string", "", tag.toString());
		tag.setTitle(NEW_TITLE);
		assertEquals("To string method should return title", NEW_TITLE, tag.toString());
	}
	
	@Test
	public void testToStringWithNoTitle() {
		final Tag tag = createNewTag();
		
		assertEquals("To string method should be empty", "", tag.toString());
	}
	
	@Test
    public void testEqualsTwoAreSameTag() {
        assertTrue("tags should be equal", defaultTag.equals(defaultTag));
    }
    
    @Test
    public void testEqualsSecondNotTag() {
        assertFalse("Objects should not be equal", defaultTag.equals(new Integer(5)));
    }
    
    @Test
    public void testEqualsTwoNotSameIdentity() {
        assertFalse("Objects should not be equal", defaultTag.equals(createNewTag(34)));
    }
    
    @Test
    public void testEqualsTwoTagSameIdentityNotSameObject() {
        assertTrue("Tags should be equal", defaultTag.equals(createNewTag()));
    }
    
    @Test
    public void testHashcode() {
        assertEquals("Hashcodes should be the same", createNewTag().hashCode(), defaultTag.hashCode());
        assertTrue("Hashcodes should not be the same", createNewTag(36).hashCode() != defaultTag.hashCode());
    }

}
