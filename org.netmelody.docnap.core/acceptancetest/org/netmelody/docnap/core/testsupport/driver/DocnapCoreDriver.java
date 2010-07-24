package org.netmelody.docnap.core.testsupport.driver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.published.Bootstrap;
import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.testsupport.StateFactory;
import org.netmelody.docnap.core.testsupport.DocumentStore;
import org.picocontainer.PicoContainer;

public class DocnapCoreDriver {
    
    private final StateFactory stateFactory;
    
    private Bootstrap bootstrapper;
    private PicoContainer context;
    private Document lastDocumentAdded;
    
    public DocnapCoreDriver(StateFactory stateFactory) {
        this.stateFactory = stateFactory;
        startUp();
    }
    
    private void startUp() {
        this.bootstrapper = new Bootstrap();
        this.context = bootstrapper.start();
    }
    
    public void setStorageLocation(String path) {
        final IDocnapStore store = context.getComponent(IDocnapStore.class);
        store.setStorageLocation(path);
    }

    public DocnapCoreDriver aRequestIsMadeTo() {
        return this;
    }
    
    public DocnapCoreDriver and() {
        return this;
    }
    
    public void theStoreIsReopened() {
        IDocnapStore store = context.getComponent(IDocnapStore.class);
        final String storageLocation = store.getStorageLocation();
        this.bootstrapper.stop();
        
        startUp();
        setStorageLocation(storageLocation);
    }

    /*
     * Adding document methods
     */
    
    public DocnapCoreDriver addADocumentForGeneratedFile() throws IOException{
        addADocumentForFile(stateFactory.aNewDocumentFile());
        return this;
    }
    
    public DocnapCoreDriver addADocumentForFile(File fileToAdd) {
        IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        this.lastDocumentAdded = documentRepository.addFile(fileToAdd);
        return this;
    }
    
    public void tagTheLastDocumentAddedWithTagTitled(String tagTitle) {
        final ITagRepository tagRepository = this.context.getComponent(ITagRepository.class);
        tagRepository.tagDocumentById(this.lastDocumentAdded.getIdentity(), tagTitle);
    }
    
    public ArrayList<DocumentStore> addNDocumentsFromGeneratedFiles(int n) throws IOException {
        return addMultipleDocumentsForFiles(stateFactory.nNewDocumentFiles(n));
    }
    
    public ArrayList<DocumentStore> addMultipleDocumentsForFiles(ArrayList<File> filesToAdd) {
        final ArrayList<DocumentStore> documents = new ArrayList<DocumentStore>();
        
        for (File fileToAdd : filesToAdd) {
            addADocumentForFile(fileToAdd);
            documents.add(new DocumentStore(this.lastDocumentAdded, this));
        }
        
        return documents;
    }
    
    /*
     * Retrieve document methods
     */
    
    public File retrieveTheFileForDocument(DocumentStore documentToRetrieve) throws IOException {
        final IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        final File storeRetreivedDocumentInFile = stateFactory.aNewEmptyFile();
        
        documentRepository.retrieveFile(documentToRetrieve.getDocument(), storeRetreivedDocumentInFile);
        
        return storeRetreivedDocumentInFile;
    }
    
    /*
     * Adding tags methods
     */
    
    public void addATagTitledToDocument(String tagTitle, DocumentStore document) {
        final ITagRepository tagRepository = this.context.getComponent(ITagRepository.class);
        
        tagRepository.tagDocumentById(document.getDocument().getIdentity(), tagTitle);
    }
    
    public void addTagsTitledToDocument(ArrayList<String> tagTitles, DocumentStore document) {
        for (String tagTitle : tagTitles) {
            addATagTitledToDocument(tagTitle, document);
        }
    }
    
    /*public void addNNewTagsToDocument(int n, DocumentStore document) {
        addTagsTitledToDocument(docnapFactory.nTagTitles(n), document);
    }*/
    
    /*
     * Add documents and tag
     */
    /*public void addDocumentWithTagsTitled(ArrayList<String> tagTitles) throws IOException {
        //addADocumentForGeneratedFile().tagTheDocumentWithTagsTitled(tagTitles);
    }*/
    
   /* public void addDocumentTaggedWithNNewTags(int n) throws IOException {
        addADocumentForGeneratedFile().tagTheDocumentWithNNewTags(n);
    }*/
    
    // TODO consider refactoring this method - doesn't feel right
   /* public void addNDocumentsAndMTagsWithLinks(int n, int m, int [][] links)  throws IOException {
        assertEquals("Links array not right size", n, links.length);
        for (int link = 0; link < links.length; link++) {
            assertTrue("Links array not right size", (m >= links[link].length));
        }
        
        ArrayList<String> tagTitles = docnapFactory.nTagTitles(m);
        
        for (int documentNumber = 0; documentNumber < n; documentNumber++) {
            DocumentStore newDocument = addADocumentForGeneratedFile();
            
            for (int documentTagNumber = 0; documentTagNumber < links[documentNumber].length; documentTagNumber++) {
                addATagTitledToDocument(tagTitles.get(links[documentNumber][documentTagNumber] - 1), newDocument);
            }
        }
    }*/
    
    public PicoContainer getContext() {
        return context;
    }

    public Collection<Document> fetchAllDocuments() {
        final IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        return documentRepository.fetchAll();
    }

    public File fetchFileFor(Document document) {
        final IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        File file = given().aNewEmptyFile();
        documentRepository.retrieveFile(document, file);
        return file;
    }
    
    private StateFactory given() {
        return this.stateFactory;
    }

    public DocnapCoreDriver containingOneDocument() {
        final File file = given().aNewPopulatedFile();
        addADocumentForFile(file);
        return this;
    }
}
