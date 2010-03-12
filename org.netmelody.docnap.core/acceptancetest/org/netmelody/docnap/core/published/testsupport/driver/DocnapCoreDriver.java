package org.netmelody.docnap.core.published.testsupport.driver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.published.testsupport.DocnapFactory;
import org.picocontainer.PicoContainer;

import static org.junit.Assert.assertEquals;

public class DocnapCoreDriver {
    
    private final PicoContainer context;
    private final DocnapFactory docnapFactory;
    
    private final ArrayList<Document> documentList = new ArrayList<Document>();

    public DocnapCoreDriver(PicoContainer context, DocnapFactory docnapFactory) {
        this.context = context;
        this.docnapFactory = docnapFactory;
    }
    
    public void addADocumentForFile(File fileToAdd) {
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        Document documentAdded = documentRepository.addFile(fileToAdd);
        
        documentList.add(documentAdded);
        
    }
    
    public void retrieveTheDocument(Document documentToRetrieve) throws IOException {
        final IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        final File storeRetreivedDocumentInFile = docnapFactory.aNewEmptyFile();
        
        
        documentRepository.retrieveFile(documentToRetrieve, storeRetreivedDocumentInFile);
        
        // TODO how to test this
        //assertThat("Incorrect file content.", FileUtils.readFileToString(storeRetreivedDocumentInFile), is(FileUtils.readFileToString(documentToRetrieve.getDocumentFile())));
    }
    
    public PicoContainer getContext() {
        return context;
    }
    
    public Document getTheNthDocumentAdded(int n) throws IOException {
        
        return documentList.get(n - 1);
        
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
}
