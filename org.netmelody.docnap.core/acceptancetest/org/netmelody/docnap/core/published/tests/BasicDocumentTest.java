package org.netmelody.docnap.core.published.tests;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.netmelody.docnap.core.published.testsupport.DocnapCoreAcceptanceTest;
import org.netmelody.docnap.core.published.testsupport.DocumentProperties;
import org.netmelody.docnap.core.published.testsupport.driver.DocnapCoreDriver;

public class BasicDocumentTest extends DocnapCoreAcceptanceTest {

    @Test
    public void supportsAddingADocument() throws IOException {
        DocnapCoreDriver docnapStore = given().aNewDocNapStore();
    }
    
    @Test
    public void supportsAddingADocumentAndRetrievingIt() throws IOException {
        final DocnapCoreDriver docnapStore = given().aNewDocNapStore();
        
        DocumentProperties documentAdded = docnapStore.addADocumentForFile(given().aNewDocumentFile());
        docnapStore.retrieveTheDocument(documentAdded);
    }
    
    /**
     * Create a new live docnap document store in a temp directory on the local file system.
     * Add a temp file to it and a tag to the document
     * 
     * Expect file content to be unchanged.
     * 
     * @throws IOException fail
     */
    @Test
    public void supportsAddingADocumentAndTaggingIt() throws IOException {
        final DocnapCoreDriver docnapStore = given().aNewDocNapStore();
        final File fileToAdd = given().aNewDocumentFile();
        final String tagTitle = given().aTagTitle();
        
        DocumentProperties document = docnapStore.addADocumentForFile(fileToAdd);
        
        docnapStore.addATagTitledToDocument(tagTitle, document);
        
        checkThatTheDocnapStore(docnapStore).isCorrect();
        //checkDocumentTags(context, document, new String[] {TAG_TITLE}); 
        
    }
}
