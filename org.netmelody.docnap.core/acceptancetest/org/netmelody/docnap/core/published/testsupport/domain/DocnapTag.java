package org.netmelody.docnap.core.published.testsupport.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Tag;


public class DocnapTag {
    
    private final Tag tag;
    
    public DocnapTag(Tag tag) {
        this.tag = tag;
    }
    
    public Tag getTag() {
        return tag;
    }
    
    public static Collection<DocnapTag> createDocnapTagCollection(Collection<Tag> tags) {
        ArrayList<DocnapTag> docnapTagList = new ArrayList<DocnapTag>();
        
        for (Tag tag : tags) {
            docnapTagList.add(new DocnapTag(tag));
        }
 
        return docnapTagList;
    }

}
