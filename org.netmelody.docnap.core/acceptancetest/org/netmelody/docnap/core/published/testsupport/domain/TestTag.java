package org.netmelody.docnap.core.published.testsupport.domain;


import org.netmelody.docnap.core.published.testsupport.checker.IDocnapTestConverter;

public class TestTag implements IDocnapTestConverter<DocnapTag> {

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
