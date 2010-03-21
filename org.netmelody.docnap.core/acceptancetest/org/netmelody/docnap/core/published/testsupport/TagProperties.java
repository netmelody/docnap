package org.netmelody.docnap.core.published.testsupport;

import java.util.HashSet;

import org.netmelody.docnap.core.repository.DocZipOutput;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagProperties {

    protected String tagPropertyLabel;
    protected final String tagTitle;
    
    private static HashSet<String> uniquePropertyLabels = new HashSet<String>();
    
    public TagProperties(String tagTitle) {
        this("", tagTitle);
    }
    
    public TagProperties(String tagPropertyLabel, String tagTitle) {
        setTagPropertyLabel(tagPropertyLabel);
        this.tagTitle = tagTitle;
    }
    
    public void setTagPropertyLabel(String tagPropertyLabel) {
        if (this instanceof DocnapTagProperties) {
            assertTrue("Label not in hash set", uniquePropertyLabels.contains(tagPropertyLabel));           
        }
        else {
            assertFalse("Label already in hash set", uniquePropertyLabels.contains(tagPropertyLabel));
        }
        
        this.tagPropertyLabel = tagPropertyLabel;
    }
    
    public static void resetUniquePropertyLabelsList() {
        uniquePropertyLabels = new HashSet<String>();
    }
    
    public boolean matchesDocnapTag(DocnapTagProperties docnapTag) {
        return tagTitle.equals(docnapTag.tag.getTitle());
    }
}
