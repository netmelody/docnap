package org.netmelody.docnap.core.published.testsuport.domain;


import java.io.IOException;

import org.netmelody.docnap.core.published.testsupport.checker.DocnapTagChecker;
import org.netmelody.docnap.core.published.testsupport.checker.IDocnapTestConverter;

public class TestTag implements IDocnapTestConverter<DocnapTag> {

    private final String title;
    
    public TestTag(String title) {
        this.title = title;
    }
    
    public boolean matchesDocnapInstance(DocnapTag docnapTag) {
        return title.equals(docnapTag.getTag().getTitle());
    }
    
    public void equalsDocnapInstance(DocnapTag docnapTag) throws IOException {
        DocnapTagChecker.checkTagProperties(this, docnapTag);
    }
    
    public String getTitle() {
        return title;
    }
}
