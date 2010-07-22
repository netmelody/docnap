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
    public final StateFactory stateFactory = new StateFactory(folder);
    
    @Before
    public final void checkTemporaryFolderExists() throws IOException {
        assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
    }
    
    public StateFactory given() {
        return this.stateFactory;
    }
    
    public DocnapCoreChecker checkThatTheDocnapStore(DocnapCoreDriver docnapStore) {
        return new DocnapCoreChecker(docnapStore, stateFactory);
    }
    
    public void checkThatTheFileRetrievedIsCorrect(TestDocument testDocument, File retrievedFile) throws IOException {
        DocnapDocumentChecker.checkThatTheFileRetrievedIsCorrect(testDocument, retrievedFile);
    }
}
