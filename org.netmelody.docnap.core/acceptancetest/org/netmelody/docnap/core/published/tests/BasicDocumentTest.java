package org.netmelody.docnap.core.published.tests;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.netmelody.docnap.core.published.testsupport.DocnapCoreAcceptanceTest;
import org.netmelody.docnap.core.published.testsupport.DocumentProperties;
import org.netmelody.docnap.core.published.testsupport.DocnapStoreTestGroup;
import org.netmelody.docnap.core.published.testsupport.driver.DocnapCoreDriver;
import org.netmelody.docnap.core.repository.DocnapStore;

public class BasicDocumentTest extends DocnapCoreAcceptanceTest {

    @Test
    public void supportsAddingADocumentAndRetrievingIt() throws IOException {
        String fileName = given().aFileName();
        File documentFile = given().aNewDocumentFileCalled(fileName);
        DocnapCoreDriver docnapStore = given().aNewDocNapStore();
        
        DocumentProperties document = docnapStore.aRequestIsMadeTo().addADocumentForFile(documentFile);
        File retrievedFile = docnapStore.aRequestIsMadeTo().retrieveTheFileForDocument(document);
        
        document.checkThatTheFileRetrievedIsCorrect(retrievedFile);
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
        
        checkThatTheDocnapStore(docnapStore).isCorrect(new DocnapStoreTestGroup());
        //checkDocumentTags(context, document, new String[] {TAG_TITLE}); 
        
    }
}
