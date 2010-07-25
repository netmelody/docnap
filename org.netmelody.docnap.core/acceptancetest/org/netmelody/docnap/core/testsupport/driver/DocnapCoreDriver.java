package org.netmelody.docnap.core.testsupport.driver;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matcher;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.Bootstrap;
import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.testsupport.StateFactory;
import org.picocontainer.PicoContainer;

public final class DocnapCoreDriver {
    
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

    public DocnapCoreDriver addADocument() {
        addADocumentForFile(stateFactory.aNewPopulatedFile());
        return this;
    }
    
    public DocnapCoreDriver addADocumentForFile(File fileToAdd) {
        IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        this.lastDocumentAdded = documentRepository.addFile(fileToAdd);
        return this;
    }
    
    public DocnapCoreDriver containingOneDocument() {
        addADocument();
        return this;
    }
    
    public void containingANumberOfDocuments(Matcher<Integer> matcher) {
        while (!matcher.matches(getNumberOfDocuments())) {
            addADocument();
        }
    }
    
    public DocnapCoreDriver containingADocumentFor(File file) {
        return addADocumentForFile(file);
    }
    
    public void tagged(String title) {
        tagTheLastDocumentAddedWithATagTitled(title);
    }

    public void tagTheLastDocumentAddedWithATagTitled(String tagTitle) {
        final ITagRepository tagRepository = this.context.getComponent(ITagRepository.class);
        tagRepository.tagDocumentById(this.lastDocumentAdded.getIdentity(), tagTitle);
    }

    public void titleTheLastDocumentAdded(String title) {
        final IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        this.lastDocumentAdded.setTitle(title);
        this.lastDocumentAdded = documentRepository.save(this.lastDocumentAdded);
    }
    
    public void changeTheLastDocumentAddedToUntagItAs(String title) {
        final ITagRepository tagRepository = this.context.getComponent(ITagRepository.class);
        tagRepository.unTagDocumentById(this.lastDocumentAdded.getIdentity(), title);
    }

    public void removeTheLastDocumentAdded() {
        IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        documentRepository.removeDocument(this.lastDocumentAdded); 
    }

    public void removeTheTagTitled(String title) {
        Tag tag = findTagByTitle(title);
        final ITagRepository tagRepository = this.context.getComponent(ITagRepository.class);
        tagRepository.removeTag(tag);
    }

    public void exportToAZipFile(File file) {
        final IDocumentRepository documentRepository = this.context.getComponent(IDocumentRepository.class);
        documentRepository.retrieveAllFilesAsZip(file);
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

    public int getNumberOfDocuments() {
        return this.context.getComponent(IDocumentRepository.class).fetchAll().size();
    }

    public int getNumberOfTags() {
        return this.context.getComponent(ITagRepository.class).fetchAll().size();
    }

    public Collection<Tag> findTagByDocumentId(Integer identity) {
        return this.context.getComponent(ITagRepository.class).findByDocumentId(identity);
    }

    public Tag findTagByTitle(String tagTitle) {
        final List<Tag> tags = this.context.getComponent(ITagRepository.class).fetchAll();
        for (Tag tag : tags) {
            if (tagTitle.equals(tag.getTitle())) {
                return tag;
            }
        }
        throw new IllegalStateException("Failed to find tag titled " + tagTitle);
    }

    public Collection<Document> findUntaggedDocuments() {
        final Collection<Document> result = new ArrayList<Document>();
        
        Collection<Document> docs = this.context.getComponent(IDocumentRepository.class).fetchAll();
        for (Document document : docs) {
            if (findTagByDocumentId(document.getIdentity()).isEmpty()) {
                result.add(document);
            }
        }
        return result;
    }
}
