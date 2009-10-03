package org.netmelody.docnap.core.published;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.domain.Document;
import org.picocontainer.PicoContainer;

public class IndexTest {

    private static final String FILE_CONTENT = "Hello, world.";

	@Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private File createTestFile() throws IOException {
    	assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
    	
    	final File testFile = this.folder.newFile("myFile.txt");
    	final BufferedWriter out = new BufferedWriter(new FileWriter(testFile));
        out.write(FILE_CONTENT);
        out.close();
        
        assertThat("Incorrect test file setup.", FileUtils.readFileToString(testFile), is(FILE_CONTENT));
        
        return testFile;
    }
    
	/**
	 * Create a new live docnap document store in a temp directory on the local file system.
	 * Add a temp file to it.
	 * Get the file back out again.
	 * 
	 * Expect file content to be unchanged.
	 * 
	 * @throws IOException fail
	 */
    @Test
	public void testCreateNewDocnapStoreAddDocumentAndRetrieveIt() throws IOException {
    	final File testFile = createTestFile();
    	
    	final PicoContainer context = new Bootstrap().start();
    	final IDocnapStore store = context.getComponent(IDocnapStore.class);
		
		final File newFolder = this.folder.newFolder("myStore");
		store.setStorageLocation(newFolder.getCanonicalPath());
		
		final IDocumentRepository repo = context.getComponent(IDocumentRepository.class);
		final Document document = repo.addDocument(testFile);
		
		final File retrievedFile = this.folder.newFile("myRetrievedFile.txt");
		repo.retrieveDocument(document, retrievedFile);
		
		assertThat("Incorrect file content.", FileUtils.readFileToString(retrievedFile), is(FILE_CONTENT));
	}
}
