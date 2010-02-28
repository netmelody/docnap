package org.netmelody.docnap.swingclient.tests;

import static org.junit.Assert.*;

import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.swingclient.TagListEntry;

import org.junit.Test;

public class TagListEntryTest {
    
    private final TagListEntry defaultTagListEntry = new TagListEntry(new Tag(1));
    
    @Test public void
    maintainsReferenceToEnclosingTag() {
        final Tag newTag = new Tag(1);
        final TagListEntry tagEntry = new TagListEntry(newTag);
        
        assertEquals("Incorrect refrerenced tag - ", newTag, tagEntry.getTag());  
    }

    @Test public void
    presentsANullTagToTheUserAsAnEmptyString() {
        final TagListEntry tagEntry = new TagListEntry(null);
        assertEquals("Incorrect representation of a null tag - ", "", tagEntry.toString());
    }
    
    @Test public void
    presentsATitlelessTagHavingDocumentsIndexedAgainstIt() {
        Tag tag = new Tag(1);
        tag.setDocumentCount(3);
        final TagListEntry tagEntry = new TagListEntry(tag);
        
        assertEquals("Incorrect representation of a title-less tag with documents -", " (3)", tagEntry.toString());
    }
    
    @Test public void
    presentsATitledTagWithNoDocumentsIndexedAgainstIt() {
        final String TAG_TITLE = "TagTitle";
        Tag tag = new Tag(1);
        tag.setTitle(TAG_TITLE);
        final TagListEntry tagEntry = new TagListEntry(tag);
        
        assertEquals("Incorrect representation of a titled tag with no documents - ", TAG_TITLE + " (0)", tagEntry.toString());
    }
    
    @Test
    public void testEqualsTwoAreSameTagListEntry() {
        assertTrue("tags should be equal", defaultTagListEntry.equals(defaultTagListEntry));
    }
    
    @Test
    public void testEqualsSecondNotTagTagListEntry() {
        assertFalse("Objects should not be equal", defaultTagListEntry.equals(new Integer(5)));
    }
    
    @Test
    public void testEqualsTwoNotSameIdentity() {
        assertFalse("Objects should not be equal", defaultTagListEntry.equals(new TagListEntry(new Tag(34))));
    }
    
    @Test
    public void testEqualsTwoTagTagListEntrySameIdentityNotSameObject() {
        assertTrue("Tags should be equal", defaultTagListEntry.equals(new TagListEntry(new Tag(1))));
    }
    
    @Test
    public void testHashcode() {
        assertEquals("Hashcodes should be the same", new TagListEntry(new Tag(1)).hashCode(), defaultTagListEntry.hashCode());
        assertTrue("Hashcodes should not be the same",new TagListEntry(new Tag(36)).hashCode() != defaultTagListEntry.hashCode());
    }

}
