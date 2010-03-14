package org.netmelody.docnap.core.published.testsupport.comparators;

import java.util.Comparator;

import org.netmelody.docnap.core.domain.Tag;

public class TagIdentityComparator implements Comparator<Tag> {

    @Override
    public int compare(Tag tag1, Tag tag2) {
        if (tag1.getIdentity() < tag2.getIdentity()) {
            return -1;
        }
        else if (tag1.getIdentity() > tag2.getIdentity()) {
            return 1;
        }
        else {
            return 0;
        }                
    }
    
}
