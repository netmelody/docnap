package org.netmelody.docnap.swingclient.tests;

import static org.junit.Assert.*;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.swingclient.TagListEntry;

import org.junit.Test;

public class TagListEntryTest {
    
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

}
