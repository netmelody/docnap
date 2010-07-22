package org.netmelody.docnap.core.testsupport.checker;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
    private final StateFactory docnapFactory;
    private final DocnapCoreDriver docnapCore;
    
    public DocnapCoreChecker(DocnapCoreDriver docnapCore, StateFactory docnapFactory) {
        this.docnapCore = docnapCore;
        this.context = docnapCore.getContext();
        this.docnapFactory = docnapFactory;
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
        
        return new TestConverter<TestDocument, DocnapDocument>(testDocuments, DocnapDocument.createDocnapTagCollection(documents, context, docnapFactory));
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

    public void hasOneDocumentContaining(File file) {
        hasTheCorrectNumberOfDocuments(1);
        Collection<Document> documents = docnapCore.fetchAllDocuments();
        assertThat(documents.iterator().next().getOriginalFilename(), is(equalTo(file.getName())));
        //TODO get and check file contents
    }

}
