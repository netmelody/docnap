package org.netmelody.docnap.core.published.tests;

import java.io.IOException;

import org.junit.Test;
import org.netmelody.docnap.core.published.testsupport.DocnapCoreAcceptanceTest;
import org.netmelody.docnap.core.published.testsupport.driver.DocnapCoreDriver;

public class BasicDocumentTest extends DocnapCoreAcceptanceTest {

    @Test
    public void supportsAddingADocument() throws IOException {
        DocnapCoreDriver docnapStore = given().aNewDocNapStore();
    }
    
    @Test
    public void supportsAddingADocumentAndRetrievingIt() throws IOException {
        final DocnapCoreDriver docnapStore = given().aNewDocNapStore();
        
        docnapStore.addADocumentForFile(given().aNewDocumentFile());
        docnapStore.retrieveTheNthDocumentAdded(1);
    }
}
