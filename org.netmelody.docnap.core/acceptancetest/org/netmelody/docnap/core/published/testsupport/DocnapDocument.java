package org.netmelody.docnap.core.published.testsupport;

import java.util.ArrayList;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.testsuport.domain.DocnapTag;

public class DocnapDocument {
    
    private Document document;
    
    public DocnapDocument(Document document) {
        this.document = document;
    }

    public static Collection<DocnapDocument> createDocnapTagCollection(Collection<Document> documents) {
        ArrayList<DocnapDocument> docnapDocumentList = new ArrayList<DocnapDocument>();
        
        for (Document document : documents) {
            docnapDocumentList.add(new DocnapDocument(document));
        }
 
        return docnapDocumentList;
    }
}
