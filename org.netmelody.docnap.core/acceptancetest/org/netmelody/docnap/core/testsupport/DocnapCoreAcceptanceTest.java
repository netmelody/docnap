package org.netmelody.docnap.core.testsupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.testsupport.checker.DocnapCoreChecker;
import org.netmelody.docnap.core.testsupport.checker.DocnapDocumentChecker;
import org.netmelody.docnap.core.testsupport.domain.TestDocument;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;

public abstract class DocnapCoreAcceptanceTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();
    
    private final StateFactory stateFactory = new StateFactory(folder);
    
    private DocnapCoreDriver docnapCore;
    private DocnapCoreChecker docnapChecker;
    
    @Before
    public final void checkTemporaryFolderExists() throws IOException {
        assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
        
        docnapCore = new DocnapCoreDriver(stateFactory);
        docnapCore.setStorageLocation(stateFactory.thePathToANewFolderForADocnapStore());
        
        docnapChecker = new DocnapCoreChecker(docnapCore, stateFactory);
    }
    
    public StateFactory given() {
        return this.stateFactory;
    }
    
    public DocnapCoreDriver when() {
        return this.docnapCore;
    };
    
    public DocnapCoreChecker then() {
        return this.docnapChecker;
    };
    
    public DocnapCoreChecker checkThatTheDocnapStore(DocnapCoreDriver docnapStore) {
        return new DocnapCoreChecker(docnapStore, stateFactory);
    }
    
    public void checkThatTheFileRetrievedIsCorrect(TestDocument testDocument, File retrievedFile) throws IOException {
        DocnapDocumentChecker.checkThatTheFileRetrievedIsCorrect(testDocument, retrievedFile);
    }
}
