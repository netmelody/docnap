package org.netmelody.docnap.core.published;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.domain.Tag;
import org.picocontainer.PicoContainer;

public class IndexTest {

    private static final String DEFAULT_FILE_CONTENT = "Hello, world.";
    private static final String DEFAULT_FILENAME = "myfile.txt";
    private static final String TAG_TITLE = "Sir Tag";
    private static final String SECOND_TITLE = "Part 2";
    private static final String A_BORING_READ = "A boring read";
    private static final String DOCUMENT_TITLE = "In the land of java";

	@Rule
    public TemporaryFolder folder = new TemporaryFolder();
	
	/**
	 * Creates a test file that has the specified file name and file content.
	 * The file will added to the temporary folder for the test and will assert
	 * that the folder exists.
	 * Also asserts that the file created has the correct content
	 * 
	 * @param fileName
	 * @param fileContent
	 * @return
	 * @throws IOException
	 */
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
	 * Creates a new docnap store in a folder called mystore inside
	 * the temporary folder for the test.
	 * Returns the context of the newly created doc nap store.
	 * 
	 * @return PicoContainer
	 */
    private PicoContainer createNewDocNapStore() throws IOException {
        return openDocNapStore(folder.newFolder("myStore").getCanonicalPath());
    }
    
    /**
     * Opens a docnap store in the folder specified inside
     * the temporary folder for the test.
     * This with create a new store if it doesn't already exist
     * 
     * Returns the context of the doc nap store.
     * 
     * @return PicoContainer
     */
    private PicoContainer openDocNapStore(String storageLocation) throws IOException {
        final PicoContainer context = new Bootstrap().start();
        final IDocnapStore store = context.getComponent(IDocnapStore.class);
        
        store.setStorageLocation(storageLocation);
        
        return context;
    }
    
    /**
     * Adds a document to the store with name myfile.txt and content
     * FILE_CONTENT
     * 
     * @param context
     * @return
     * @throws IOException
     */
    private Document addDocument(PicoContainer context) throws IOException {
        return addDocument(context, DEFAULT_FILENAME, DEFAULT_FILE_CONTENT);
    }
    
    /**
     * Adds a document to the store with the specified filename and file content
     * Returns the document added
     * 
     * @param context
     * @param fileName
     * @param fileContent
     * @return
     * @throws IOException
     */
    private Document addDocument(PicoContainer context, String fileName, String fileContent) throws IOException {
        final File testFile = createTestFile(fileName, fileContent);
        
        final IDocumentRepository repo = context.getComponent(IDocumentRepository.class);
        final Document document = repo.addFile(testFile);
        return document;
    }
    
    private void retrieveDocument(PicoContainer context, Document document) throws IOException {
        retrieveDocument(context, document, "myRetrievedFile.txt", DEFAULT_FILE_CONTENT);
    }
    
    private void retrieveDocument(PicoContainer context, Document document, String fileName, String fileContent) throws IOException {
        final IDocumentRepository repo = context.getComponent(IDocumentRepository.class);
        
        final File retrievedFile = this.folder.newFile(fileName);
        repo.retrieveFile(document, retrievedFile);
        
        assertThat("Incorrect file content.", FileUtils.readFileToString(retrievedFile), is(fileContent));
    }
    
    private Document addDocumentAndRetrieveIt(PicoContainer context) throws IOException {
        final Document document = addDocument(context);
        retrieveDocument(context, document);
        return document;
    }
    
    private void checkDocumentTags(PicoContainer context, Document document, String[] tagTitles) {
      final ITagRepository tagRepository = context.getComponent(ITagRepository.class);
      
      Collection<Tag> documentTags = tagRepository.findByDocumentId(document.getIdentity());
      assertEquals("Incorrect Number of Tags for document", tagTitles.length, documentTags.size());
      
      Collections.sort((List<Tag>)documentTags, new TagCompare());
      
      int title = 0;
      for (Tag tag : documentTags) {
         assertNotNull("Tag should not be null", tag);
         assertEquals("Tag title should be as specified", tagTitles[title], tag.getTitle());
         title++;
      }
    }
    
    private void checkDocumentProperties(Document document, String title, String originalFileName) {
        if (null == title) {
            assertNull("Document Title should be null", document.getTitle());
        }
        else {
            assertEquals("Document title should match", title, document.getTitle());
        }
        
        if (null == originalFileName) {
            assertNull("Document Original file name should be null", document.getOriginalFilename());
        }
        else {
            assertEquals("Document original file name should match", originalFileName, document.getOriginalFilename());
        }
    }
    
    private void checkDocuments(PicoContainer context, String[] contents, 
                                String[] documentTitles, String[] originalFileNames,
                                String[][] tagTitles) throws IOException {
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        Integer documentCount = documentRepository.getNumberOfDocuments();
        assertEquals("Document Count should be same as number of documents", new Integer(contents.length), documentCount);
        
        Collection<Document> documents = documentRepository.fetchAll();
        assertEquals("Document collection should contain all documents", contents.length, documents.size());
        
        Collections.sort((List<Document>)documents, new DocumentCompare());
        
        int documentIndex = 0;
        for (Document document : documents) {
            retrieveDocument(context, document, document.getOriginalFilename(), contents[documentIndex]);
            checkDocumentProperties(document, documentTitles[documentIndex], originalFileNames[documentIndex]);
            checkDocumentTags(context, document, tagTitles[documentIndex]);
            documentIndex++;
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
                
        final ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        
        tagRepository.tagDocumentById(firstDocument.getIdentity(), TAG_TITLE);
        
        checkDocumentTags(context, firstDocument, new String[] {TAG_TITLE});
        checkDocumentTags(context, secondDocument, new String[] {});
        
        retrieveDocument(context, firstDocument, "myRetrievedFile.txt", DEFAULT_FILE_CONTENT);
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
        
        PicoContainer secondContext = openDocNapStore(storageLocation); 
        checkDocuments(secondContext, new String[] {DEFAULT_FILE_CONTENT}, new String[] {null}, 
                       new String[] {DEFAULT_FILENAME}, new String[][] {{}});
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
        
        PicoContainer secondContext = openDocNapStore(storageLocation); 
        
        checkDocumentTags(secondContext, firstDocument, new String[] {TAG_TITLE});
        checkDocumentTags(secondContext, secondDocument, new String[] {TAG_TITLE, SECOND_TITLE});
        
    }
    
    /**
     * Create a new live docnap document store in a temp directory on the local file system.
     * Add a file to it.
     * Change some of the file properties 
     * Save
     * Check file properties
     * 
     * @throws IOException fail
     */
    @Test
    public void testCreateDocnapStoreAddFileChangeandSave() throws IOException {
        PicoContainer context = createNewDocNapStore();
      
        final Document document = addDocument(context);
        
        checkDocumentProperties(document, null, DEFAULT_FILENAME);
        
        document.setTitle(DOCUMENT_TITLE);
        
        final IDocumentRepository docRepository = context.getComponent(IDocumentRepository.class);
        final Document savedDocument = docRepository.save(document);
        checkDocumentProperties(savedDocument, DOCUMENT_TITLE, DEFAULT_FILENAME); 
    }
    
    private class DocumentCompare implements Comparator<Document> {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(Document doc1, Document doc2) {
            if (doc1.getIdentity() < doc2.getIdentity())
                return -1;
            else if (doc1.getIdentity() > doc2.getIdentity()) {
                return 1;
            }
            else {
                return 0;
            }                
        }
        
    }
    
    private class TagCompare implements Comparator<Tag> {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(Tag tag1, Tag tag2) {
            if (tag1.getIdentity() < tag2.getIdentity())
                return -1;
            else if (tag1.getIdentity() > tag2.getIdentity()) {
                return 1;
            }
            else {
                return 0;
            }                
        }
        
    }
    
}
