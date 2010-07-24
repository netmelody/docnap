package org.netmelody.docnap.core.testsupport.checker;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.netmelody.docnap.core.testsupport.matcher.FileContentsMatcher.hasContentsEqualTo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.testsupport.StateFactory;
import org.netmelody.docnap.core.testsupport.domain.DocnapDocument;
import org.netmelody.docnap.core.testsupport.domain.DocnapStoreTestGroup;
import org.netmelody.docnap.core.testsupport.domain.DocnapTag;
import org.netmelody.docnap.core.testsupport.domain.TestDocument;
import org.netmelody.docnap.core.testsupport.domain.TestTag;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;
import org.picocontainer.PicoContainer;

public class DocnapCoreChecker {
    
    private final PicoContainer context;
    private final StateFactory stateFactory;
    private final DocnapCoreDriver docnapCore;
    private Document lastDocumentAccessed;
    
    public DocnapCoreChecker(DocnapCoreDriver docnapCore, StateFactory stateFactory) {
        this.docnapCore = docnapCore;
        this.context = docnapCore.getContext();
        this.stateFactory = stateFactory;
    }
    
    public void isCorrect(DocnapStoreTestGroup testStore) throws IOException {
        hasTheCorrectNumberOfDocuments(testStore.getNumberOfDocuments());
        hasTheCorrectNumberOfTags(testStore.getNumberOfTags());
        
        TestConverter<TestTag, DocnapTag> tagConverter = matchTags(testStore.getTags());
        TestConverter<TestDocument, DocnapDocument> documentConverter = matchDocuments(testStore.getDocuments());

        tagConverter.checkMappingsEqual(new DocnapTagChecker(testStore, context.getComponent(ITagRepository.class)));
        documentConverter.checkMappingsEqual(new DocnapDocumentChecker(testStore, tagConverter, context.getComponent(ITagRepository.class)));
    }
    
    private TestConverter<TestTag, DocnapTag> matchTags(Collection<TestTag> testTags) throws IOException {
        ITagRepository tagRepository = context.getComponent(ITagRepository.class);

        Collection<Tag> tags = tagRepository.fetchAll();

        return new TestConverter<TestTag, DocnapTag>(testTags, DocnapTag.createDocnapTagCollection(tags));
    }
    
    private TestConverter<TestDocument, DocnapDocument> matchDocuments(Collection<TestDocument> testDocuments) throws IOException {
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);

        Collection<Document> documents = documentRepository.fetchAll();
        
        return new TestConverter<TestDocument, DocnapDocument>(testDocuments, DocnapDocument.createDocnapTagCollection(documents, context, stateFactory));
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
