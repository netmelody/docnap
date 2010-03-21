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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        return new DocumentProperties(fileToAdd, documentAdded, this);
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
    
    public File retrieveTheDocument(DocumentProperties documentToRetrieve) throws IOException {
        final IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        final File storeRetreivedDocumentInFile = docnapFactory.aNewEmptyFile();
        
        documentRepository.retrieveFile(documentToRetrieve.getDocument(), storeRetreivedDocumentInFile);
        
        assertThat("Incorrect file content.", FileUtils.readFileToString(storeRetreivedDocumentInFile), is(FileUtils.readFileToString(documentToRetrieve.getFile())));
        return storeRetreivedDocumentInFile;
    }
    
    /*
     * Adding tags methods
     */
    
    public void addATagTitledToDocument(String tagTitle, DocumentProperties document) {
        final ITagRepository tagRepository = this.context.getComponent(ITagRepository.class);
        
        tagRepository.tagDocumentById(document.getDocument().getIdentity(), tagTitle);
    }
    
    public void addTagsTitledToDocument(ArrayList<String> tagTitles, DocumentProperties document) {
        for (String tagTitle : tagTitles) {
            addATagTitledToDocument(tagTitle, document);
        }
    }
    
    public void addNNewTagsToDocument(int n, DocumentProperties document) {
        addTagsTitledToDocument(docnapFactory.nTagTitles(n), document);
    }
    
    /*
     * Add documents and tag
     */
    public void addDocumentWithTagsTitled(ArrayList<String> tagTitles) throws IOException {
        addADocumentForGeneratedFile().tagTheDocumentWithTagsTitled(tagTitles);
    }
    
    public void addDocumentTaggedWithNNewTags(int n) throws IOException {
        addADocumentForGeneratedFile().tagTheDocumentWithNNewTags(n);
    }
    
    // TODO consider refactoring this method - doesn't feel right
    public void addNDocumentsAndMTagsWithLinks(int n, int m, int [][] links)  throws IOException {
        assertEquals("Links array not right size", n, links.length);
        for (int link = 0; link < links.length; link++) {
            assertTrue("Links array not right size", (m >= links[link].length));
        }
        
        ArrayList<String> tagTitles = docnapFactory.nTagTitles(m);
        
        for (int documentNumber = 0; documentNumber < n; documentNumber++) {
            DocumentProperties newDocument = addADocumentForGeneratedFile();
            
            for (int documentTagNumber = 0; documentTagNumber < links[documentNumber].length; documentTagNumber++) {
                addATagTitledToDocument(tagTitles.get(links[documentNumber][documentTagNumber] - 1), newDocument);
            }
        }
    }
    
    public PicoContainer getContext() {
        return context;
    }
}
