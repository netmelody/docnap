package org.netmelody.docnap.core.published.testsupport;

import org.netmelody.docnap.core.domain.Tag;


public class DocnapTagProperties extends TagProperties{
    
    public final Tag tag;
    
    public DocnapTagProperties(Tag tag) {
        this("", tag);
    }
    
    public DocnapTagProperties(String tagPropertyLabel, Tag tag) {
        super(tagPropertyLabel, tag.getTitle());
        this.tag = tag;
    }

}
