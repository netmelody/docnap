package org.netmelody.docnap.core.published.testsupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.published.testsupport.checker.DocnapCoreChecker;
import org.netmelody.docnap.core.published.testsupport.checker.DocnapDocumentChecker;
import org.netmelody.docnap.core.published.testsupport.driver.DocnapCoreDriver;

public abstract class DocnapCoreAcceptanceTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();
    
    public DocnapFactory docnapFactory;
    
    @Before
    public final void checkTemporaryFolderExists() throws IOException {
        assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
        docnapFactory = new DocnapFactory(folder);
    }
    
    public DocnapFactory given() {
        return docnapFactory;
    }
    
    public DocnapCoreChecker checkThatTheDocnapStore(DocnapCoreDriver docnapStore) {
        return new DocnapCoreChecker(docnapStore, docnapFactory);
    }
    
    public void checkThatTheFileRetrievedIsCorrect(TestDocument testDocument, File retrievedFile) throws IOException {
        new DocnapDocumentChecker().checkThatTheFileRetrievedIsCorrect(testDocument, retrievedFile);
    }
}
