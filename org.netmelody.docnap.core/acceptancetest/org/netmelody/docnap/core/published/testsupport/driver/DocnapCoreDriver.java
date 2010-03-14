package org.netmelody.docnap.core.published.testsupport.driver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.published.testsupport.DocnapFactory;
import org.netmelody.docnap.core.published.testsupport.DocumentProperties;
import org.picocontainer.PicoContainer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DocnapCoreDriver {
    
    private final PicoContainer context;
    private final DocnapFactory docnapFactory;
    
    public DocnapCoreDriver(PicoContainer context, DocnapFactory docnapFactory) {
        this.context = context;
        this.docnapFactory = docnapFactory;
    }
    
    /*
     * Adding document methods
     */
    
    public DocumentProperties addADocumentForGeneratedFile() throws IOException{
        return addADocumentForFile(docnapFactory.aNewDocumentFile());
    }
    
    public DocumentProperties addADocumentForFile(File fileToAdd) {
        IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        
        Document documentAdded = documentRepository.addFile(fileToAdd);
        return new DocumentProperties(fileToAdd, documentAdded);
    }
    
    public ArrayList<DocumentProperties> addNDocumentsFromGeneratedFiles(int n) throws IOException {
        return addMultipleDocumentsForFiles(docnapFactory.nNewDocumentFiles(n));
    }
    
    public ArrayList<DocumentProperties> addMultipleDocumentsForFiles(ArrayList<File> filesToAdd) {
        final ArrayList<DocumentProperties> documents = new ArrayList<DocumentProperties>();
        
        for (File fileToAdd : filesToAdd) {
            documents.add(addADocumentForFile(fileToAdd));
        }
        
        return documents;
    }
    
    /*
     * Retrieve document methods
     */
    
    public void retrieveTheDocument(DocumentProperties documentToRetrieve) throws IOException {
        final IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        final File storeRetreivedDocumentInFile = docnapFactory.aNewEmptyFile();
        
        documentRepository.retrieveFile(documentToRetrieve.getDocument(), storeRetreivedDocumentInFile);
        
        assertThat("Incorrect file content.", FileUtils.readFileToString(storeRetreivedDocumentInFile), is(FileUtils.readFileToString(documentToRetrieve.getFile())));
    }
    
    public void addATagTitledToDocument(String tagTitle, DocumentProperties document) {
        final ITagRepository tagRepository = this.context.getComponent(ITagRepository.class);
        
        tagRepository.tagDocumentById(document.getDocument().getIdentity(), tagTitle);
    }
    
    public PicoContainer getContext() {
        return context;
    }
}
