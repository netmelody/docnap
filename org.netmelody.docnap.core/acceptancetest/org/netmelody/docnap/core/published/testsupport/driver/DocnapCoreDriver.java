package org.netmelody.docnap.core.published.testsupport.driver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.published.testsupport.DocnapFactory;
import org.picocontainer.PicoContainer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class DocnapCoreDriver {
    
    private final PicoContainer context;
    private final DocnapFactory docnapFactory;
    
    private final ArrayList<DocumentProperties> documentList = new ArrayList<DocumentProperties>();

    public DocnapCoreDriver(PicoContainer context, DocnapFactory docnapFactory) {
        this.context = context;
        this.docnapFactory = docnapFactory;
    }
    
    public void addADocumentForFile(File fileToAdd) {
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        Document documentAdded = documentRepository.addFile(fileToAdd);
        
        documentList.add(new DocumentProperties(documentAdded, fileToAdd));
        
    }
    
    public void retrieveTheNthDocumentAdded(int n) throws IOException {
        final IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        final File storeRetreivedDocumentInFile = docnapFactory.aNewEmptyFile();
        
        final DocumentProperties documentToRetrieve = documentList.get(n - 1);
        
        documentRepository.retrieveFile(documentToRetrieve.getDocument(), storeRetreivedDocumentInFile);
        
        assertThat("Incorrect file content.", FileUtils.readFileToString(storeRetreivedDocumentInFile), is(FileUtils.readFileToString(documentToRetrieve.getDocumentFile())));
    }
    
    public PicoContainer getContext() {
        return context;
    }
    
    
    /*
     * Checking methods
     */
    
    public void hasTheCorrectNumberOfDocuments(int numberOfDocuments) {
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        assertEquals("Incorrect number of documents in the store", numberOfDocuments, documentRepository.getNumberOfDocuments().intValue());
    }
    
    public void hasTheCorrectNumberOfTags(int numberOfTags) {
        ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        
        assertEquals("Incorrect number of documents in the store", numberOfTags, tagRepository.fetchAll().size());
    }
    
    private class DocumentProperties {
        
        private final File documentFile;
        private final Document document;
        
        public DocumentProperties(Document document, File documentFile) {
            this.documentFile = documentFile;
            this.document = document;
        }
        
        public File getDocumentFile() {
            return this.documentFile;
        }
        
        public Document getDocument() {
            return this.document;
        }
    }
}
