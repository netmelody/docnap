package org.netmelody.docnap.core.published.tests;

import java.io.IOException;

import org.junit.Test;
import org.netmelody.docnap.core.published.Bootstrap;
import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.testsupport.DocnapCoreAcceptanceTest;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;
import org.picocontainer.PicoContainer;

public class DocnapTest extends DocnapCoreAcceptanceTest{

    @Test
    public void supportsCreatingANewDocnapStoreWithStorageLocation() throws IOException {
        // TODO what should we assert here?
        final PicoContainer context = new Bootstrap().start();
        final IDocnapStore store = context.getComponent(IDocnapStore.class);
        
        store.setStorageLocation(given().thePathToANewFolderForADocnapStore());
    }
    
    @Test
    public void supportsCreatingANewDocnapStore() throws IOException {
        final DocnapCoreDriver docnapStore = given().aNewDocNapStore();
        
        checkThatTheDocnapStore(docnapStore).hasTheCorrectNumberOfDocuments(0);
        checkThatTheDocnapStore(docnapStore).hasTheCorrectNumberOfTags(0);
    }
    
    @Test
    // TODO finish the test when have added documents
    public void supportsCreatingANewDocnapStoreClosingItAndReopeningIt() throws IOException {
        final DocnapCoreDriver docnapStore = given().aNewDocNapStore();
        
        checkThatTheDocnapStore(docnapStore).hasTheCorrectNumberOfDocuments(0);
        checkThatTheDocnapStore(docnapStore).hasTheCorrectNumberOfTags(0);
    }
    
}
