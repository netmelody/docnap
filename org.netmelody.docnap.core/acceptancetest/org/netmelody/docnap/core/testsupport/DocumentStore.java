package org.netmelody.docnap.core.testsupport;

import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;

public class DocumentStore {

    private final Document document;
    private final DocnapCoreDriver docnapStore;
    
    public DocumentStore(Document document, DocnapCoreDriver docnapStore) {
        this.document = document;
        this.docnapStore = docnapStore;
    }
    
    public Document getDocument() {
        return document;
    }
    
    public DocumentStore and() {
        return this;
    }
    
    public void tagWithTagTitled(String tagTitle) {
        docnapStore.addATagTitledToDocument(tagTitle, this);
    }
}
