package org.netmelody.docnap.core.testsupport.checker;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.netmelody.docnap.core.testsupport.matcher.FileContentsMatcher.hasContentsEqualTo;

import java.io.File;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;
import org.picocontainer.PicoContainer;

public class DocnapCoreChecker {
    
    private final PicoContainer context;
    private final DocnapCoreDriver docnapCore;
    private Document lastDocumentAccessed;
    
    public DocnapCoreChecker(DocnapCoreDriver docnapCore) {
        this.docnapCore = docnapCore;
        this.context = docnapCore.getContext();
    }

    private void hasTheCorrectNumberOfDocuments(int numberOfDocuments) {
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        assertEquals("Incorrect number of documents in the store", numberOfDocuments, documentRepository.getNumberOfDocuments().intValue());
    }
    
    private void hasTheCorrectNumberOfTags(int numberOfTags) {
        ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        assertEquals("Incorrect number of tags in the store", numberOfTags, tagRepository.fetchAll().size());
    }

    public DocnapCoreChecker theStore() {
        return this;
    }

    public void isEmpty() {
        hasTheCorrectNumberOfDocuments(0);
        hasTheCorrectNumberOfTags(0);
    }
    
    public void hasOneDocument() {
        hasTheCorrectNumberOfDocuments(1);
        Collection<Document> documents = docnapCore.fetchAllDocuments();
        this.lastDocumentAccessed = documents.iterator().next();
    }

    public DocnapCoreChecker hasOneDocumentContaining(File file) {
        theStore().hasOneDocument();
        assertThat(this.lastDocumentAccessed.getOriginalFilename(), is(equalTo(file.getName())));
        
        final File documentFile = docnapCore.fetchFileFor(this.lastDocumentAccessed);
        assertThat(documentFile, hasContentsEqualTo(file));
        return this;
    }
    
    public void tagged(String tagTitle) {
        final ITagRepository tagRepository = context.getComponent(ITagRepository.class);
        final Collection<Tag> tags = tagRepository.findByDocumentId(this.lastDocumentAccessed.getIdentity());
        
        assertThat(tags.size(), is(equalTo(1)));
        assertThat(tags.iterator().next().getTitle(), is(equalTo(tagTitle)));
    }
}
