package org.netmelody.docnap.core.published.testsupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.published.testsupport.driver.DocnapCoreDriver;

public class DocumentProperties {

    private final Document document;
    private final File file;
    private final DocnapCoreDriver docnapStore;
    
    public DocumentProperties(File file, Document document, DocnapCoreDriver docnapStore) {
        this.file = file;
        this.document = document;
        this.docnapStore = docnapStore;
    }
    
    public Document getDocument() {
        return document;
    }
    
    public File getFile() {
        return file;
    }
    
    public void andRetrieveTheDocumentFile() throws IOException {
        docnapStore.retrieveTheDocument(this);
    }
    
    public void tagTheDocumentWithTagTitled(String tagTitle) {
        docnapStore.addATagTitledToDocument(tagTitle, this);
    }
    
    public void tagTheDocumentWithTagsTitled(ArrayList<String> tagTitles) {
        docnapStore.addTagsTitledToDocument(tagTitles, this);
    }
    
    public void tagTheDocumentWithNNewTags(int n) {
        docnapStore.addNNewTagsToDocument(n, this);
    }
}
