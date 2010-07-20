package org.netmelody.docnap.core.published.tests;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.netmelody.docnap.core.testsupport.DocnapCoreAcceptanceTest;
import org.netmelody.docnap.core.testsupport.DocumentStore;
import org.netmelody.docnap.core.testsupport.domain.DocnapStoreTestGroup;
import org.netmelody.docnap.core.testsupport.domain.TestDocument;
import org.netmelody.docnap.core.testsupport.domain.TestTag;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;

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
        
        checkThatTheFileRetrievedIsCorrect(testDocument, retrievedFile);
        checkThatTheDocnapStore(docnapStore).isCorrect(new DocnapStoreTestGroup(testDocument));
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
        TestDocument testDocument = given().aNewTestDocument();
        TestTag testTag = given().aNewTestTag();
        DocnapCoreDriver docnapStore = given().aNewDocNapStore();
        
        docnapStore.aRequestIsMadeTo().addADocumentForFile(testDocument.getFile()).and().tagWithTagTitled(testTag.getTitle());
        
        checkThatTheDocnapStore(docnapStore).isCorrect(new DocnapStoreTestGroup(testDocument, testTag, true));
        
    }
}
