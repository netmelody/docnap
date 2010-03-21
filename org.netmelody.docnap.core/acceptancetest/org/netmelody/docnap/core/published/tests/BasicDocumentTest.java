package org.netmelody.docnap.core.published.tests;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.netmelody.docnap.core.published.testsupport.DocnapCoreAcceptanceTest;
import org.netmelody.docnap.core.published.testsupport.DocnapStoreTestGroup;
import org.netmelody.docnap.core.published.testsupport.DocumentStore;
import org.netmelody.docnap.core.published.testsupport.TestDocument;
import org.netmelody.docnap.core.published.testsupport.driver.DocnapCoreDriver;

public class BasicDocumentTest extends DocnapCoreAcceptanceTest {
    
    @Test
    public void supportsAddingADocument() throws IOException {
        TestDocument testDocument = given().aNewTestDocument();
        DocnapCoreDriver docnapStore = given().aNewDocNapStore();
        
        docnapStore.aRequestIsMadeTo().addADocumentForFile(testDocument.getFile());
        
        checkThatTheDocnapStore(docnapStore).isCorrect(new DocnapStoreTestGroup(testDocument));
    }

    @Test
    public void supportsAddingADocumentAndRetrievingIt() throws IOException {
        TestDocument testDocument = given().aNewTestDocument();
        DocnapCoreDriver docnapStore = given().aNewDocNapStore();
        
        DocumentStore document = docnapStore.aRequestIsMadeTo().addADocumentForFile(testDocument.getFile());
        File retrievedFile = docnapStore.aRequestIsMadeTo().retrieveTheFileForDocument(document);
        
        checkThatTheFileRetrievedIsCorrect(document, retrievedFile);
        checkThatTheDocnapStore(docnapStore).isCorrect(new DocnapStoreTestGroup());
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
        
        DocumentStore document = docnapStore.addADocumentForFile(fileToAdd);
        
        docnapStore.addATagTitledToDocument(tagTitle, document);
        
        checkThatTheDocnapStore(docnapStore).isCorrect(new DocnapStoreTestGroup());
        //checkDocumentTags(context, document, new String[] {TAG_TITLE}); 
        
    }
}
