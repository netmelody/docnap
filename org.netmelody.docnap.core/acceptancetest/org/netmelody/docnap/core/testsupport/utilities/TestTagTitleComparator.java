package org.netmelody.docnap.core.testsupport.utilities;

import java.util.Comparator;

import org.netmelody.docnap.core.published.testsupport.domain.TestTag;

public class TestTagTitleComparator implements Comparator<TestTag>{
    
    public int compare(TestTag tag1, TestTag tag2) {
        return tag1.getTitle().compareTo(tag2.getTitle());  
    }

}
