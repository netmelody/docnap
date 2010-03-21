package org.netmelody.docnap.core.published.testsupport;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
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
    
    public File retrieveTheDocumentFile() throws IOException {
        return docnapStore.retrieveTheDocument(this);
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
    
    /*
     * Checker methods
     */
    
    public void checkThatTheFileRetrievedIsCorrect(File retrievedFile) throws IOException {
        assertThat("Incorrect file content.", FileUtils.readFileToString(retrievedFile), is(FileUtils.readFileToString(file)));
    }
}
