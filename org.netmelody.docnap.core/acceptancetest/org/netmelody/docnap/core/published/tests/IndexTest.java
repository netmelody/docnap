package org.netmelody.docnap.core.published.tests;

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
import org.netmelody.docnap.core.published.Bootstrap;
import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.picocontainer.PicoContainer;

public class IndexTest {

    private static final String DEFAULT_FILE_CONTENT = "Hello, world.";
    private static final String SECOND_FILE_CONTENT = "A boring read";
    private static final String THIRD_FILE_CONTENT = "this is a very important document";
    
    private static final String DEFAULT_FILENAME = "myfile.txt";
    
    private static final String TAG_TITLE = "Sir Tag";
    private static final String SECOND_TAG_TITLE = "Part 2";
    private static final String THIRD_TAG_TITLE = "Part 56";
    
    private static final String DOCUMENT_TITLE = "In the land of java";
    
    private static final String DOC_NAME_1 = "DocFile1.lst";
    private static final String DOC_NAME_2 = "DocFile2.lst";
    private static final String DOC_NAME_3 = "DocFile3.lst";
    private static final String DOC_NAME_4 = "DocFile4.lst";
    private static final String DOC_NAME_5 = "DocFile5.lst";
    
    private static final String DOC_CONTENT_1 = "Doc content 1";
    private static final String DOC_CONTENT_2 = "Content of 2";
    private static final String DOC_CONTENT_3 = "Doc 3 content";
    private static final String DOC_CONTENT_4 = "4 has content";
    private static final String DOC_CONTENT_5 = "Some more content for 5";
    private static final String DOC_CONTENT_6 = "Different content for 6";
    private static final String DOC_CONTENT_7 = "Content to be 7";
    
    private static final String[] TAG_TITLES = {"Tag title A", "Tag Title B", "Tag Title C", "Tag Title D"};
    private static final String[] DOCUMENT_NAMES = {DOC_NAME_1, DOC_NAME_2, DOC_NAME_3, DOC_NAME_4, DOC_NAME_5};
    private static final String[] DOCUMENT_TITLES = {"Doc Title 1", "Doc Title 2", "Doc Title 3", "Doc Title 4"};
    private static final String[] DOCUMENT_CONTENTS = {DOC_CONTENT_1, DOC_CONTENT_2, DOC_CONTENT_3, DOC_CONTENT_4, DOC_CONTENT_5};

	@Rule
    public TemporaryFolder folder = new TemporaryFolder();
	
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
    
    /**
     * Give a document a title and save it
     * Returns the newly saved document
     * 
     * @param context
     * @param Document
     * @param DocumentTitle
     * @return Document
     * @throws IOException
     */
    private Document setDocumentTitleAndSave(PicoContainer context, Document document, String documentTitle) {
        final IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        document.setTitle(documentTitle);
        return (documentRepository.save(document));
    }
    
    /**
     * Tag the given document with the tag title supplied
     * The tag that the document is taged with is returned
     * 
     * @param context
     * @param Document
     * @param TagTitle
     * @return Tag
     * @throws IOException
     */
    private Tag tagDocumentWithTagTitle(PicoContainer context, Document document, String tagTitle) {
        final ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        
        return (tagRepository.tagDocumentById(document.getIdentity(), tagTitle));
        
    }
    
    /**
     * Retrieve the specified document and check that it has the correct content
     * which is the default file content
     * 
     * @param context
     * @param Document
     * @return 
     * @throws IOException
     */
    private void retrieveDocument(PicoContainer context, Document document) throws IOException {
        retrieveDocument(context, document, "myRetrievedFile.txt", DEFAULT_FILE_CONTENT);
    }
    
    /**
     * Retrieve the specified document and check that it has the correct content
     * as specified
     * 
     * @param context
     * @param Document
     * @param fileName
     * @param fileContent
     * @return 
     * @throws IOException
     */
    private void retrieveDocument(PicoContainer context, Document document, String fileName, String fileContent) throws IOException {
        final IDocumentRepository repo = context.getComponent(IDocumentRepository.class);
        
        final File retrievedFile = this.folder.newFile(fileName);
        repo.retrieveFile(document, retrievedFile);
        
        assertThat("Incorrect file content.", FileUtils.readFileToString(retrievedFile), is(fileContent));
    }
    
    /**
     * Add a document to the store and then retrieve it and check that it 
     * has the correct content.
     * Returns the document added
     * 
     * @param context
     * @param Document
     * @return 
     * @throws IOException
     */
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
    
    private void checkDocumentsInDocnapStore(PicoContainer context, String[] contents, 
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
    
    private void checkTagProperties(Tag tag, String tagTitle) {
        assertNotNull("Tag should not be null", tag);
        assertEquals("Tag title should be as specified", tagTitle, tag.getTitle());
    }
    
    private void checkTagsInDocnapStore(PicoContainer context, String[] tagTitles) throws IOException {
        ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        
        Collection<Tag> tags = tagRepository.fetchAll();
        assertEquals("Tag collectin should contain all tags", tagTitles.length, tags.size());
        
        Collections.sort((List<Tag>)tags, new TagCompare());
        
        int tagIndex = 0;
        for (Tag tag : tags) {
            checkTagProperties(tag, tagTitles[tagIndex]);
            tagIndex++;
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
        Document secondDocument = addDocument(context, "name.lst", SECOND_FILE_CONTENT);
                
        final ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        
        tagRepository.tagDocumentById(firstDocument.getIdentity(), TAG_TITLE);
        
        checkDocumentTags(context, firstDocument, new String[] {TAG_TITLE});
        checkDocumentTags(context, secondDocument, new String[] {});
        
        retrieveDocument(context, firstDocument, "myRetrievedFile.txt", DEFAULT_FILE_CONTENT);
        retrieveDocument(context, secondDocument, "myRetrievedFile2.txt", SECOND_FILE_CONTENT);
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
        checkDocumentsInDocnapStore(secondContext, new String[] {DEFAULT_FILE_CONTENT}, new String[] {null}, 
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
        final Document secondDocument = addDocument(context, "second.doc", SECOND_FILE_CONTENT);
        firstTagRepository.tagDocumentById(firstDocument.getIdentity(), TAG_TITLE);
        firstTagRepository.tagDocumentById(secondDocument.getIdentity(), SECOND_TAG_TITLE);
        firstTagRepository.tagDocumentById(secondDocument.getIdentity(), TAG_TITLE);
        
        checkDocumentTags(context, firstDocument, new String[] {TAG_TITLE});
        checkDocumentTags(context, secondDocument, new String[] {TAG_TITLE, SECOND_TAG_TITLE});
        
        context = null;
        
        PicoContainer secondContext = openDocNapStore(storageLocation); 
        
        checkDocumentTags(secondContext, firstDocument, new String[] {TAG_TITLE});
        checkDocumentTags(secondContext, secondDocument, new String[] {TAG_TITLE, SECOND_TAG_TITLE});
        
    }
    
    @Test
    public void testCreateDocnapStoreAddTagsAndRemoveTags() throws IOException {
        PicoContainer context = createNewDocNapStore();
        
        final Document firstDocument = addDocument(context);
        final Document secondDocument = addDocument(context, "another.txt", SECOND_FILE_CONTENT);
        final Document thirdDocument = addDocument(context, "moremore.lst", THIRD_FILE_CONTENT);
        
        tagDocumentWithTagTitle(context, firstDocument, TAG_TITLE);
        Tag tagToRemove = tagDocumentWithTagTitle(context, firstDocument, SECOND_TAG_TITLE);
        tagDocumentWithTagTitle(context, secondDocument, TAG_TITLE);
        tagDocumentWithTagTitle(context, secondDocument, THIRD_TAG_TITLE);
        tagDocumentWithTagTitle(context, thirdDocument, SECOND_TAG_TITLE);
        
        checkTagsInDocnapStore(context, new String[] {TAG_TITLE, SECOND_TAG_TITLE, THIRD_TAG_TITLE});
        checkDocumentsInDocnapStore(context, 
                                    new String[] {DEFAULT_FILE_CONTENT, SECOND_FILE_CONTENT, THIRD_FILE_CONTENT},
                                    new String[] {null, null, null},
                                    new String[] {DEFAULT_FILENAME, "another.txt", "moremore.lst"}, 
                                    new String[][] {{TAG_TITLE, SECOND_TAG_TITLE}, {TAG_TITLE, THIRD_TAG_TITLE}, {SECOND_TAG_TITLE}});
        
        ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        tagRepository.removeTag(tagToRemove);
        
        checkTagsInDocnapStore(context, new String[] {TAG_TITLE, THIRD_TAG_TITLE});
        checkDocumentsInDocnapStore(context, 
                                    new String[] {DEFAULT_FILE_CONTENT, SECOND_FILE_CONTENT, THIRD_FILE_CONTENT},
                                    new String[] {null, null, null},
                                    new String[] {DEFAULT_FILENAME, "another.txt", "moremore.lst"}, 
                                    new String[][] {{TAG_TITLE}, {TAG_TITLE, THIRD_TAG_TITLE}, {}});

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
    
    /**
     * Create a new live docnap document store in a temp directory on the local file system.
     * Add a file to it.
     * Close and reopen
     * Change some of the file properties 
     * Save
     * Check file properties
     * Close and reopen
     * Check file properites
     * 
     * @throws IOException fail
     */
    @Test
    public void testCreateDocnapStoreAddFileChangeandSaveWithReopens() throws IOException {
        PicoContainer firstContext = createNewDocNapStore();
        final String storageLocation = firstContext.getComponent(IDocnapStore.class).getStorageLocation();
      
        final Document document = addDocument(firstContext);   
        final ITagRepository tagRepository = firstContext.getComponent(ITagRepository.class);
        tagRepository.tagDocumentById(document.getIdentity(), TAG_TITLE);
        checkDocumentProperties(document, null, DEFAULT_FILENAME);
        firstContext = null;
        
        PicoContainer secondContext = openDocNapStore(storageLocation);
        final IDocumentRepository docRepository = secondContext.getComponent(IDocumentRepository.class);
        document.setTitle(DOCUMENT_TITLE);
        final Document savedDocument = docRepository.save(document);
        checkDocumentProperties(savedDocument, DOCUMENT_TITLE, DEFAULT_FILENAME); 
        
        secondContext = null;
        
        PicoContainer thirdContext = openDocNapStore(storageLocation);
        
        checkDocumentsInDocnapStore(thirdContext, new String[] {DEFAULT_FILE_CONTENT}, new String[] {DOCUMENT_TITLE},
                       new String[] {DEFAULT_FILENAME}, new String[][]{{TAG_TITLE}});
        
    }
    
    /**
     * Create a new live docnap document store in a temp directory on the local file system.
     * Add a files to it.
     * Add tags to the files
     * Close and reopen
     * Check the documents and their tags
     * Check the tags
     * Check the number of documents
     * @throws IOException fail
     */
    @Test
    public void testCreateDocnapStoreAddFilesAndTagsReopenCheck() throws IOException {
        PicoContainer context = createNewDocNapStore();
        final String storageLocation = context.getComponent(IDocnapStore.class).getStorageLocation();
      
        final Document document1 = addDocument(context, DOCUMENT_NAMES[0], DEFAULT_FILE_CONTENT); 
        
        tagDocumentWithTagTitle(context, document1, TAG_TITLES[0]);
        tagDocumentWithTagTitle(context, document1, TAG_TITLES[1]);
        
        final Document document2 = addDocument(context, DOCUMENT_NAMES[1], DEFAULT_FILE_CONTENT);
        
        tagDocumentWithTagTitle(context, document2, TAG_TITLES[1]);
        tagDocumentWithTagTitle(context, document1, TAG_TITLES[2]);
        
        final Document document3 = addDocument(context, DOCUMENT_NAMES[2], DEFAULT_FILE_CONTENT);
        
        tagDocumentWithTagTitle(context, document1, TAG_TITLES[3]);
        setDocumentTitleAndSave(context, document2, DOCUMENT_TITLES[1]);
        setDocumentTitleAndSave(context, document3, DOCUMENT_TITLES[2]);
        
        tagDocumentWithTagTitle(context, document2, TAG_TITLES[3]);
        
        final Document document4 = addDocument(context, DOCUMENT_NAMES[3], DEFAULT_FILE_CONTENT);
        tagDocumentWithTagTitle(context, document4, TAG_TITLES[1]);
        tagDocumentWithTagTitle(context, document4, TAG_TITLES[2]);

    
        context = null;
              
        PicoContainer secondContext = openDocNapStore(storageLocation);
        
        checkDocumentsInDocnapStore(secondContext, 
                       new String[] {DEFAULT_FILE_CONTENT, DEFAULT_FILE_CONTENT, DEFAULT_FILE_CONTENT, DEFAULT_FILE_CONTENT}, 
                       new String[] {null, DOCUMENT_TITLES[1], DOCUMENT_TITLES[2], null},
                       new String[] {DOCUMENT_NAMES[0], DOCUMENT_NAMES[1], DOCUMENT_NAMES[2], DOCUMENT_NAMES[3]},
                       new String[][]{{TAG_TITLES[0], TAG_TITLES[1], TAG_TITLES[2], TAG_TITLES[3]},
                                      {TAG_TITLES[1], TAG_TITLES[3]}, {},
                                      {TAG_TITLES[1], TAG_TITLES[2]}});
        
    }
    
    /**
     * Create a new docnap store and save all files to zip
     * @throws IOException fail
     */
    @Test
    public void testZipNoFiles() throws IOException{
        
        PicoContainer context = createNewDocNapStore();
        
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        documentRepository.retrieveAllFilesAsZip(this.folder.newFile("test.zip"));
    }
    
    /**
     * Create a new docnap store add some documents with different
     * file names and then try to 
     * zip the files
     */
    @Test
    public void testZipFilesDifferentNames() throws IOException {
        PicoContainer context = createNewDocNapStore();
        
        final int numberOfFiles = 4;
        final String[] documentNames = new String[numberOfFiles];
        final String[] documentContents = new String[numberOfFiles];
        for (int doc = 0; doc < numberOfFiles; doc++) {
            addDocument(context, DOCUMENT_NAMES[doc], DOCUMENT_CONTENTS[doc]);
            documentNames[doc] = DOCUMENT_NAMES[doc];
            documentContents[doc] = DOCUMENT_CONTENTS[doc];
        }
        
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        File zipFile = this.folder.newFile("test.zip");
        documentRepository.retrieveAllFilesAsZip(zipFile);
        ZipInputTestHelper.checkZipFile(zipFile, this.folder, documentNames, documentContents);
    }
    
    /**
     * Create a new docnap store add some documents two with the same name
     * zip the files
     */
    @Test
    public void testZipFilesTwoSameName() throws IOException {
        PicoContainer context = createNewDocNapStore();
        
        final String SAME_NAME = "DocSame.lst";
        final String CONVERTED_SAME_NAME = "DocSame_1.lst";
        final String[] documentNames = {DOC_NAME_1, SAME_NAME, DOC_NAME_3, SAME_NAME};
        final String[] documentContents = {DOC_CONTENT_1, DOC_CONTENT_2, DOC_CONTENT_3, DOC_CONTENT_4};
        for (int doc = 0; doc < documentNames.length; doc++) {
            addDocument(context, documentNames[doc], documentContents[doc]);
        }
        
        final String[] zipDocumentNames = {DOC_NAME_1, DOC_NAME_3, SAME_NAME, CONVERTED_SAME_NAME};
        final String[] zipDocumentContents = {DOC_CONTENT_1, DOC_CONTENT_3, DOC_CONTENT_2, DOC_CONTENT_4};
        
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        File zipFile = this.folder.newFile("test.zip");
        documentRepository.retrieveAllFilesAsZip(zipFile);
        ZipInputTestHelper.checkZipFile(zipFile, this.folder, zipDocumentNames, zipDocumentContents);
    }
    
    /**
     * Create a new docnap store add some documents two with the same name
     * and already a file with the convert to name
     */
    @Test
    public void testZipFilesTwoSameNameAlreadyHaveFileWithConvertedName() throws IOException {
        PicoContainer context = createNewDocNapStore();
        
        final String SAME_NAME = "DocSame.lst";
        final String CONVERTED_SAME_NAME = "DocSame_1.lst";
        final String SECOND_CONVERTED_SAME_NAME = "DocSame_2.lst";
        final String[] documentNames = {DOC_NAME_1, SAME_NAME, DOC_NAME_3, SAME_NAME, CONVERTED_SAME_NAME};
        final String[] documentContents = {DOC_CONTENT_1, DOC_CONTENT_2, DOC_CONTENT_3, DOC_CONTENT_4, DOC_CONTENT_5};
        for (int doc = 0; doc < documentNames.length; doc++) {
            addDocument(context, documentNames[doc], documentContents[doc]);
        }
        
        final String[] zipDocumentNames = {DOC_NAME_1, DOC_NAME_3, SAME_NAME, CONVERTED_SAME_NAME, SECOND_CONVERTED_SAME_NAME};
        final String[] zipDocumentContents = {DOC_CONTENT_1, DOC_CONTENT_3, DOC_CONTENT_2, DOC_CONTENT_5, DOC_CONTENT_4};
        
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        File zipFile = this.folder.newFile("test.zip");
        documentRepository.retrieveAllFilesAsZip(zipFile);
        ZipInputTestHelper.checkZipFile(zipFile, this.folder, zipDocumentNames, zipDocumentContents);
    }
    
    /**
     * Create a new docnap store add some documents two with the same name
     * and already a file with the convert to name and second convert
     * also have two files with the convert name
     */
    @Test
    public void testZipFilesTwoSameNameAlreadyHaveFileWithConvertedNameAndSecondConvert() throws IOException {
        PicoContainer context = createNewDocNapStore();
        
        final String SAME_NAME = "DocSame.lst";
        final String CONVERTED_SAME_NAME = "DocSame_1.lst";
        final String SECOND_CONVERTED_SAME_NAME = "DocSame_2.lst";
        final String THIRD_CONVERTED_SAME_NAME = "DocSame_3.lst";
        final String CONVERTED_CONVERTED_SAME_NAME = "DocSame_1_1.lst";
        final String[] documentNames = {DOC_NAME_1, SECOND_CONVERTED_SAME_NAME, SAME_NAME, CONVERTED_SAME_NAME, DOC_NAME_3, SAME_NAME, CONVERTED_SAME_NAME};
        final String[] documentContents = {DOC_CONTENT_1, DOC_CONTENT_2, DOC_CONTENT_3, DOC_CONTENT_4, DOC_CONTENT_5, DOC_CONTENT_6, DOC_CONTENT_7};
        for (int doc = 0; doc < documentNames.length; doc++) {
            addDocument(context, documentNames[doc], documentContents[doc]);
        }
        
        final String[] zipDocumentNames = {DOC_NAME_1, DOC_NAME_3, SAME_NAME, CONVERTED_SAME_NAME, CONVERTED_CONVERTED_SAME_NAME, SECOND_CONVERTED_SAME_NAME, THIRD_CONVERTED_SAME_NAME};
        final String[] zipDocumentContents = {DOC_CONTENT_1, DOC_CONTENT_5, DOC_CONTENT_3, DOC_CONTENT_4, DOC_CONTENT_7, DOC_CONTENT_2, DOC_CONTENT_6};
        
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        File zipFile = this.folder.newFile("test.zip");
        documentRepository.retrieveAllFilesAsZip(zipFile);
        ZipInputTestHelper.checkZipFile(zipFile, this.folder, zipDocumentNames, zipDocumentContents);
    }
    
    /**
    * Create a new docnap store add some documents and tag them
    * Then remove a tag from two documents
    */
    @Test
    public void testCreateNewDocnapStoreAddDocumentsWithTagsAndUnTagDocuments() throws IOException {
        PicoContainer context = createNewDocNapStore();
      
        Document document1 = addDocument(context, DOC_NAME_1, DOC_CONTENT_1);
        tagDocumentWithTagTitle(context, document1, TAG_TITLE);
        tagDocumentWithTagTitle(context, document1, SECOND_TAG_TITLE);
        Document document2 = addDocument(context, DOC_NAME_2, DOC_CONTENT_2);
        tagDocumentWithTagTitle(context, document2, TAG_TITLE);
        Document document3 = addDocument(context, DOC_NAME_3, DOC_CONTENT_3);
        tagDocumentWithTagTitle(context, document3, SECOND_TAG_TITLE);
        Document document4 = addDocument(context, DOC_NAME_4, DOC_CONTENT_4);
        tagDocumentWithTagTitle(context, document4, THIRD_TAG_TITLE);
        
        checkDocumentsInDocnapStore(context, 
                                    new String[] {DOC_CONTENT_1, DOC_CONTENT_2, DOC_CONTENT_3, DOC_CONTENT_4},
                                    new String[] {null, null, null, null},
                                    new String[] {DOC_NAME_1, DOC_NAME_2, DOC_NAME_3, DOC_NAME_4},
                                    new String[][] {{TAG_TITLE, SECOND_TAG_TITLE},{TAG_TITLE}, {SECOND_TAG_TITLE}, {THIRD_TAG_TITLE}});
    
        ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        
        tagRepository.unTagDocumentById(document2.getIdentity(), TAG_TITLE);
        tagRepository.unTagDocumentById(document4.getIdentity(), THIRD_TAG_TITLE);
    
        checkDocumentsInDocnapStore(context, 
                new String[] {DOC_CONTENT_1, DOC_CONTENT_2, DOC_CONTENT_3, DOC_CONTENT_4},
                new String[] {null, null, null, null},
                new String[] {DOC_NAME_1, DOC_NAME_2, DOC_NAME_3, DOC_NAME_4},
                new String[][] {{TAG_TITLE, SECOND_TAG_TITLE},{}, {SECOND_TAG_TITLE}, {}});

    }
    
    /**
     * Create a new docnap store add some documents and tag them
     * Close and reopen the docnap store
     * Then remove a tag from two documents
     */
    @Test
    public void testCreateNewDocnapStoreAddDocumentsWithTagsReopenAndUnTagDocument() throws IOException {
        PicoContainer context = createNewDocNapStore();
        final String storageLocation = context.getComponent(IDocnapStore.class).getStorageLocation();
      
        Document document1 = addDocument(context, DOC_NAME_1, DOC_CONTENT_1);
        tagDocumentWithTagTitle(context, document1, TAG_TITLE);
        tagDocumentWithTagTitle(context, document1, SECOND_TAG_TITLE);
        Document document2 = addDocument(context, DOC_NAME_2, DOC_CONTENT_2);
        tagDocumentWithTagTitle(context, document2, TAG_TITLE);
        Document document3 = addDocument(context, DOC_NAME_3, DOC_CONTENT_3);
        tagDocumentWithTagTitle(context, document3, SECOND_TAG_TITLE);
        Document document4 = addDocument(context, DOC_NAME_4, DOC_CONTENT_4);
        tagDocumentWithTagTitle(context, document4, THIRD_TAG_TITLE);
        
        checkDocumentsInDocnapStore(context, 
                                    new String[] {DOC_CONTENT_1, DOC_CONTENT_2, DOC_CONTENT_3, DOC_CONTENT_4},
                                    new String[] {null, null, null, null},
                                    new String[] {DOC_NAME_1, DOC_NAME_2, DOC_NAME_3, DOC_NAME_4},
                                    new String[][] {{TAG_TITLE, SECOND_TAG_TITLE},{TAG_TITLE}, {SECOND_TAG_TITLE}, {THIRD_TAG_TITLE}});
    
        context = null;
        
        PicoContainer secondContext = openDocNapStore(storageLocation);
        
        ITagRepository tagRepository = secondContext.getComponent(ITagRepository.class);
        
        tagRepository.unTagDocumentById(document2.getIdentity(), TAG_TITLE);
        tagRepository.unTagDocumentById(document4.getIdentity(), THIRD_TAG_TITLE);
    
        checkDocumentsInDocnapStore(secondContext, 
                new String[] {DOC_CONTENT_1, DOC_CONTENT_2, DOC_CONTENT_3, DOC_CONTENT_4},
                new String[] {null, null, null, null},
                new String[] {DOC_NAME_1, DOC_NAME_2, DOC_NAME_3, DOC_NAME_4},
                new String[][] {{TAG_TITLE, SECOND_TAG_TITLE},{}, {SECOND_TAG_TITLE}, {}});

    }
    
    /**
     * Create a new docnap store add two documents and give
     * each a different tag
     * Try and remove a nonexistent tag from the first document
     */
    @Test
    public void testCreateNewDocnapStoreAddTwoDocumentsWithTagsUnTagDocumentWithInvalidTag() throws IOException {
        PicoContainer context = createNewDocNapStore();
      
        Document document1 = addDocument(context, DOC_NAME_1, DOC_CONTENT_1);
        tagDocumentWithTagTitle(context, document1, TAG_TITLE);
        Document document2 = addDocument(context, DOC_NAME_2, DOC_CONTENT_2);
        tagDocumentWithTagTitle(context, document2, SECOND_TAG_TITLE);
        
        checkDocumentsInDocnapStore(context, 
                                    new String[] {DOC_CONTENT_1, DOC_CONTENT_2},
                                    new String[] {null, null},
                                    new String[] {DOC_NAME_1, DOC_NAME_2},
                                    new String[][] {{TAG_TITLE}, {SECOND_TAG_TITLE}});
    
        ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        
        tagRepository.unTagDocumentById(document1.getIdentity(), "TagNotExist");
    
        checkDocumentsInDocnapStore(context, 
                new String[] {DOC_CONTENT_1, DOC_CONTENT_2},
                new String[] {null, null},
                new String[] {DOC_NAME_1, DOC_NAME_2},
                new String[][] {{TAG_TITLE}, {SECOND_TAG_TITLE}});

    }
    
    
    private class DocumentCompare implements Comparator<Document> {

        @Override
        public int compare(Document doc1, Document doc2) {
            if (doc1.getIdentity() < doc2.getIdentity()) {
                return -1;
            }
            else if (doc1.getIdentity() > doc2.getIdentity()) {
                return 1;
            }
            else {
                return 0;
            }                
        }
        
    }
    
    private class TagCompare implements Comparator<Tag> {

        @Override
        public int compare(Tag tag1, Tag tag2) {
            if (tag1.getIdentity() < tag2.getIdentity()) {
                return -1;
            }
            else if (tag1.getIdentity() > tag2.getIdentity()) {
                return 1;
            }
            else {
                return 0;
            }                
        }
        
    }
    
}
