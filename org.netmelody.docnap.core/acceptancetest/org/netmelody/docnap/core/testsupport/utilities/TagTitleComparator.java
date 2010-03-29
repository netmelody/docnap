package org.netmelody.docnap.core.testsupport.utilities;

import java.util.Comparator;

import org.netmelody.docnap.core.domain.Tag;

public class TagTitleComparator implements Comparator<Tag>{
    
    public int compare(Tag tag1, Tag tag2) {
        return tag1.getTitle().compareTo(tag2.getTitle());  
    }
}
