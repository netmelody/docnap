package org.netmelody.docnap.core.published.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
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

    private static final String TAG_TITLE = "Sir Tag";
    private static final String SECOND_TAG_TITLE = "Part 2";
    private static final String THIRD_TAG_TITLE = "Part 56";
    
    private static final String DOC_NAME_1 = "DocFile1.lst";
    private static final String DOC_NAME_2 = "DocFile2.lst";
    private static final String DOC_NAME_3 = "DocFile3.lst";
    private static final String DOC_NAME_4 = "DocFile4.lst";
    
    private static final String DOC_CONTENT_1 = "Doc content 1";
    private static final String DOC_CONTENT_2 = "Content of 2";
    private static final String DOC_CONTENT_3 = "Doc 3 content";
    private static final String DOC_CONTENT_4 = "4 has content";
    
	@Rule
    public TemporaryFolder folder = new TemporaryFolder();
	
    @Before
    public final void checkTemporaryFolderExists() throws IOException {
        assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
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
