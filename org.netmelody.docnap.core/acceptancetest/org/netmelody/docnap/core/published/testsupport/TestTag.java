package org.netmelody.docnap.core.published.testsupport;


import org.netmelody.docnap.core.published.testsuport.domain.DocnapTag;

public class TestTag implements DocnapMatcher<DocnapTag> {

    private final String tagTitle;
    
    public TestTag(String tagTitle) {
        this.tagTitle = tagTitle;
    }
    
    public boolean matchesDocnapInstance(DocnapTag docnapTag) {
        return tagTitle.equals(docnapTag.getTag().getTitle());
    }
    
    public String getTagTitle() {
        return tagTitle;
    }
}
