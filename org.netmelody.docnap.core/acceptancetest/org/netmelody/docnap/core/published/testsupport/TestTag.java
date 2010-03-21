package org.netmelody.docnap.core.published.testsupport;


import org.netmelody.docnap.core.published.testsuport.domain.DocnapTag;

public class TestTag implements DocnapMatcher<DocnapTag> {

    private final String title;
    
    public TestTag(String title) {
        this.title = title;
    }
    
    public boolean matchesDocnapInstance(DocnapTag docnapTag) {
        return title.equals(docnapTag.getTag().getTitle());
    }
    
    public String getTitle() {
        return title;
    }
}
