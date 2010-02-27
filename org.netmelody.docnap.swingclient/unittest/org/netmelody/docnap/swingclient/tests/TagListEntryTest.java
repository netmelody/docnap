package org.netmelody.docnap.swingclient.tests;

import static org.junit.Assert.*;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.swingclient.TagListEntry;

import org.junit.Test;

public class TagListEntryTest {
    
    @Test
    public void testInitialisation() {
        final Tag newTag = new Tag(1);
        final TagListEntry tagEntry = new TagListEntry(newTag);
        
        assertEquals("Tag should be set", newTag, tagEntry.getTag());  
    }

    @Test
    public void testToStringNullTag() {
        final TagListEntry tagEntry = new TagListEntry(null);
        assertEquals("To String should be the empty string", "", tagEntry.toString());
    }
    
    @Test
    public void testToStringNullTitle() {
        Tag tag = new Tag(1);
        tag.setDocumentCount(3);
        final TagListEntry tagEntry = new TagListEntry(tag);
        
        assertEquals("To String should set", " (3)", tagEntry.toString());
    }
    
    @Test
    public void testToStringTitleSet() {
        final String TAG_TITLE = "TagTitle";
        Tag tag = new Tag(1);
        tag.setTitle(TAG_TITLE);
        final TagListEntry tagEntry = new TagListEntry(tag);
        
        assertEquals("To String should set", TAG_TITLE + " (0)", tagEntry.toString());
    }

}
