package org.netmelody.docnap.core.published.testsupport.comparators;

import java.util.Comparator;

import org.netmelody.docnap.core.domain.Document;

public class DocumentIdentityComparator implements Comparator<Document> {

    @Override
    public int compare(Document doc1, Document doc2) {
        if (doc1.getIdentity() < doc2.getIdentity()) {
            return -1;
        }
        else if (doc1.getIdentity() > doc2.getIdentity()) {
            return 1;
        }
        else {
            return 0;
        }                
    }
    
}
