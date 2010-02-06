package org.netmelody.docnap.core.published;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.domain.Tag;
import org.picocontainer.PicoContainer;

public class IndexTest {

    private static final String FILE_CONTENT = "Hello, world.";
    private static final String TAG_TITLE = "Sir Tag";
    private static final String A_BORING_READ = "A boring read";

	@Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private File createTestFile() throws IOException {
        return(createTestFile("myfile.txt", FILE_CONTENT));
    }
	
	private File createTestFile(String fileName, String fileContent) throws IOException {
    	assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
    	
    	final File testFile = this.folder.newFile(fileName);
    	final BufferedWriter out = new BufferedWriter(new FileWriter(testFile));
        out.write(fileContent);
        out.close();
        
        assertThat("Incorrect test file setup.", FileUtils.readFileToString(testFile), is(fileContent));
        
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
    	final newDocNapStore newDocStore = new newDocNapStore();
        
        final File testFile = createTestFile();
    	
    	final PicoContainer context = newDocStore.getContext();
		
		final IDocumentRepository repo = context.getComponent(IDocumentRepository.class);
		final Document document = repo.addDocument(testFile);
		
		final File retrievedFile = this.folder.newFile("myRetrievedFile.txt");
		repo.retrieveDocument(document, retrievedFile);
		
		assertThat("Incorrect file content.", FileUtils.readFileToString(retrievedFile), is(FILE_CONTENT));
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
	public void testCreateNewDocnapStoreAddDocumentAndTagIt() throws IOException {
    	final File testFile = createTestFile();
    	
    	final PicoContainer context = new Bootstrap().start();
    	final IDocnapStore store = context.getComponent(IDocnapStore.class);
		
		final File newFolder = this.folder.newFolder("myStore");
		store.setStorageLocation(newFolder.getCanonicalPath());
		
		final IDocumentRepository documentRepo = context.getComponent(IDocumentRepository.class);
		final Document document = documentRepo.addDocument(testFile);
		
		final ITagRepository tagRepository = context.getComponent(ITagRepository.class);
		tagRepository.tagDocumentById(document.getIdentity(), TAG_TITLE);
		
		Collection<Tag> documentTags = tagRepository.findByDocumentId(document.getIdentity());
	
		assertEquals("Incorrect Number of Tags", 1, documentTags.size());
		for (Tag tag : documentTags) {
			assertNotNull("Tag should not be null", tag);
			assertEquals("Tag title should be as specified", TAG_TITLE, tag.getTitle());
		}
		
		
	}
    
    /**
     * Create a new live docnap document store in a temp directory on the local file system.
     * Add a temp file to it and a tag to the document
     * Add a second file with no tag
     * 
     * Expect file content to be unchanged.
     * 
     * @throws IOException fail
     */
    @Test
    public void testCreateNewDocnapStoreTwoDocumentsTagOne() throws IOException {
        final File testFile = createTestFile();
        
        final PicoContainer context = new Bootstrap().start();
        final IDocnapStore store = context.getComponent(IDocnapStore.class);
        
        final File newFolder = this.folder.newFolder("myStore");
        store.setStorageLocation(newFolder.getCanonicalPath());
        
        final IDocumentRepository documentRepo = context.getComponent(IDocumentRepository.class);
        final Document firstDocument = documentRepo.addDocument(testFile);
        
        final ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        tagRepository.tagDocumentById(firstDocument.getIdentity(), TAG_TITLE);
        
        final File secondTestFile = createTestFile("name.lst", "A boring read");
        final Document secondDocument = documentRepo.addDocument(secondTestFile);
        
        Collection<Tag> documentTags = tagRepository.findByDocumentId(firstDocument.getIdentity());
        assertEquals("Incorrect Number of Tags for document 1", 1, documentTags.size());
        for (Tag tag : documentTags) {
            assertNotNull("Tag should not be null", tag);
            assertEquals("Tag title should be as specified", TAG_TITLE, tag.getTitle());
        }
        
        documentTags = tagRepository.findByDocumentId(secondDocument.getIdentity());
        assertEquals("Incorrect Number of Tags for document 2", 0, documentTags.size());
        
        final File retrievedFile = this.folder.newFile("myRetrievedFile.txt");
        documentRepo.retrieveDocument(firstDocument, retrievedFile);
        assertThat("Incorrect file content for document 1.", FileUtils.readFileToString(retrievedFile), is(FILE_CONTENT));
        
        final File retrievedSecondFile = this.folder.newFile("myRetrievedFile2.txt");
        documentRepo.retrieveDocument(secondDocument, retrievedSecondFile);
        assertThat("Incorrect file content for document 2.", FileUtils.readFileToString(retrievedSecondFile), is(A_BORING_READ));
    }
    
    /**
     * Create a new live docnap document store in a temp directory on the local file system.
     * Add a temp file to it.
     * Then delete it
     * 
     * Expect file content to be unchanged.
     * 
     * @throws IOException fail
     */
    @Test
    public void testCreateNewDocnapStoreAddDocumentAndRemoveIt() throws IOException {
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
    
    class newDocNapStore {
        
        private final PicoContainer context;
        private final IDocnapStore store;
        private final File newFolder;
        
        public newDocNapStore() throws IOException {
            context = new Bootstrap().start();
            store = context.getComponent(IDocnapStore.class);
            
            newFolder = folder.newFolder("myStore");
            store.setStorageLocation(newFolder.getCanonicalPath());
        }

        /**
         * @return the context
         */
        public PicoContainer getContext() {
            return context;
        }

        /**
         * @return the store
         */
        public IDocnapStore getStore() {
            return store;
        }

        /**
         * @return the newFolder
         */
        public File getNewFolder() {
            return newFolder;
        }
        
        
    }
}
