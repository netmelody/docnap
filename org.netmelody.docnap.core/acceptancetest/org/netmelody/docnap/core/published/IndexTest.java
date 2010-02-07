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
    private static final String SECOND_TITLE = "Part 2";
    private static final String A_BORING_READ = "A boring read";

	@Rule
    public TemporaryFolder folder = new TemporaryFolder();
	
	private File createTestFile(String fileName, String fileContent) throws IOException {
    	assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
    	
    	final File testFile = this.folder.newFile(fileName);
    	final BufferedWriter out = new BufferedWriter(new FileWriter(testFile));
        out.write(fileContent);
        out.close();
        
        assertThat("Incorrect test file setup.", FileUtils.readFileToString(testFile), is(fileContent));
        
        return testFile;
    }
	
    private PicoContainer createNewDocNapStore() throws IOException {
        final PicoContainer context = new Bootstrap().start();
        final IDocnapStore store = context.getComponent(IDocnapStore.class);
        
        final File newFolder = folder.newFolder("myStore");
        store.setStorageLocation(newFolder.getCanonicalPath());
        
        return context;
    }
    
    private PicoContainer reopenDocNapStore(String storageLocation) throws IOException {
        final PicoContainer context = new Bootstrap().start();
        final IDocnapStore store = context.getComponent(IDocnapStore.class);
        
        store.setStorageLocation(storageLocation);
        
        return context;
    }
    
    private Document addDocument(PicoContainer context) throws IOException {
        return addDocument(context, "myfile.txt", FILE_CONTENT);
    }
    
    private Document addDocument(PicoContainer context, String fileName, String fileContent) throws IOException {
        final File testFile = createTestFile(fileName, fileContent);
        
        final IDocumentRepository repo = context.getComponent(IDocumentRepository.class);
        final Document document = repo.addFile(testFile);
        return document;
    }
    
    private void retrieveDocument(PicoContainer context, Document document) throws IOException {
        retrieveDocument(context, document, "myRetrievedFile.txt", FILE_CONTENT);
    }
    
    private void retrieveDocument(PicoContainer context, Document document, String fileName, String fileContent) throws IOException {
        final IDocumentRepository repo = context.getComponent(IDocumentRepository.class);
        
        final File retrievedFile = this.folder.newFile(fileName);
        repo.retrieveFile(document, retrievedFile);
        
        assertThat("Incorrect file content.", FileUtils.readFileToString(retrievedFile), is(fileContent));
    }
    
    private Document addDocumentAndRetrieveIt(PicoContainer context) throws IOException {
        Document document = addDocument(context);
        
        retrieveDocument(context, document);
        return document;
    }
    
    private void checkDocumentTags(PicoContainer context, Document document, String[] titles) {
      final ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        
      Collection<Tag> documentTags = tagRepository.findByDocumentId(document.getIdentity());
      assertEquals("Incorrect Number of Tags for document", titles.length, documentTags.size());
      int title = 0;
      for (Tag tag : documentTags) {
         assertNotNull("Tag should not be null", tag);
         assertEquals("Tag title should be as specified", titles[title], tag.getTitle());
         title++;
      }
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
        final PicoContainer context = createNewDocNapStore();
    	
    	addDocumentAndRetrieveIt(context);
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
    	final PicoContainer context = createNewDocNapStore();
        
    	Document document = addDocument(context);
    		
		final ITagRepository tagRepository = context.getComponent(ITagRepository.class);
		tagRepository.tagDocumentById(document.getIdentity(), TAG_TITLE);
		
		checkDocumentTags(context, document, new String[] {TAG_TITLE});	
		
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
        final PicoContainer context = createNewDocNapStore();
        
        Document firstDocument = addDocument(context);
        Document secondDocument = addDocument(context, "name.lst", A_BORING_READ);
                
        final IDocumentRepository documentRepo = context.getComponent(IDocumentRepository.class);
        final ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        
        tagRepository.tagDocumentById(firstDocument.getIdentity(), TAG_TITLE);
        
        checkDocumentTags(context, firstDocument, new String[] {TAG_TITLE});
        checkDocumentTags(context, secondDocument, new String[] {});
        
        retrieveDocument(context, firstDocument, "myRetrievedFile.txt", FILE_CONTENT);
        retrieveDocument(context, secondDocument, "myRetrievedFile2.txt", A_BORING_READ);
    }
    
    /**
     * Create a new live docnap document store in a temp directory on the local file system.
     * Add a temp file to it.
     * Then delete it
     * 
     * 
     * @throws IOException fail
     */
    @Test
    public void testCreateNewDocnapStoreAddDocumentAndRemoveIt() throws IOException {
        final PicoContainer context = createNewDocNapStore();
        
        Document document = addDocumentAndRetrieveIt(context);
        
        IDocumentRepository documentRepo = context.getComponent(IDocumentRepository.class);
        
        assertEquals("There should be one document", new Integer(1), documentRepo.getNumberOfDocuments());
        
        documentRepo.removeDocument(document);
        
        assertEquals("There should be no documents", new Integer(0), documentRepo.getNumberOfDocuments());        
    }
    
    /**
     * Create a new live docnap document store in a temp directory on the local file system.
     * Add a file to it.
     * Restart
     * File should still be there
     * 
     * @throws IOException fail
     */
    @Test
    public void testCreateNewDocnapStoreAddDocumentReStart() throws IOException {
        PicoContainer context = createNewDocNapStore();
        final String storageLocation = context.getComponent(IDocnapStore.class).getStorageLocation();
       
        Document document = addDocumentAndRetrieveIt(context);
        
        context = null;
        
        PicoContainer secondContext = reopenDocNapStore(storageLocation); 
        retrieveDocument(secondContext, document);
        
    }
    
    /**
     * Create a new live docnap document store in a temp directory on the local file system.
     * Add a file to it.
     * Add a tag to the file
     * Add another file
     * Add two tags to the file
     * Restart
     * Files should still be there with the same tags
     * 
     * @throws IOException fail
     */
    @Test
    public void testCreateNewDocnapStoreAddDocumentsWithTagsReStart() throws IOException {
        PicoContainer context = createNewDocNapStore();
        String storageLocation = context.getComponent(IDocnapStore.class).getStorageLocation();
        final ITagRepository firstTagRepository = context.getComponent(ITagRepository.class);
       
        final Document firstDocument = addDocument(context);
        final Document secondDocument = addDocument(context, "second.doc", A_BORING_READ);
        firstTagRepository.tagDocumentById(firstDocument.getIdentity(), TAG_TITLE);
        firstTagRepository.tagDocumentById(secondDocument.getIdentity(), SECOND_TITLE);
        firstTagRepository.tagDocumentById(secondDocument.getIdentity(), TAG_TITLE);
        
        checkDocumentTags(context, firstDocument, new String[] {TAG_TITLE});
        /* TODO currently dependent on order retrieved 
         * Order select or order collection?
         */
        checkDocumentTags(context, secondDocument, new String[] {TAG_TITLE, SECOND_TITLE});
        
        context = null;
        
        PicoContainer secondContext = reopenDocNapStore(storageLocation); 
        
        checkDocumentTags(secondContext, firstDocument, new String[] {TAG_TITLE});
        checkDocumentTags(secondContext, secondDocument, new String[] {TAG_TITLE, SECOND_TITLE});
        
    }
    
}
