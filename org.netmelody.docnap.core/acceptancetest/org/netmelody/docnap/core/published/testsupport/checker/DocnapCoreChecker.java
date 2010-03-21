package org.netmelody.docnap.core.published.testsupport.checker;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.published.testsuport.domain.DocnapTag;
import org.netmelody.docnap.core.published.testsupport.DocnapDocument;
import org.netmelody.docnap.core.published.testsupport.TestConverter;
import org.netmelody.docnap.core.published.testsupport.DocnapStoreTestGroup;
import org.netmelody.docnap.core.published.testsupport.TestDocument;
import org.netmelody.docnap.core.published.testsupport.TestTag;
import org.netmelody.docnap.core.published.testsupport.driver.DocnapCoreDriver;
import org.picocontainer.PicoContainer;

public class DocnapCoreChecker {
    
    private final PicoContainer context;
    
    public DocnapCoreChecker(DocnapCoreDriver docnapStore) {
        this.context = docnapStore.getContext();
    }
    
    public void isCorrect(DocnapStoreTestGroup testStore) {
        hasTheCorrectNumberOfDocuments(testStore.getNumberOfDocuments());
        hasTheCorrectNumberOfTags(testStore.getNumberOfTags());
        
        matchTags(testStore.getTags());
        matchDocuments(testStore.getDocuments());
    }
    
    private TestConverter<TestTag, DocnapTag> matchTags(ArrayList<TestTag> testTags) {
        ITagRepository tagRepository = context.getComponent(ITagRepository.class);

        Collection<Tag> tags = tagRepository.fetchAll();

        return new TestConverter<TestTag, DocnapTag>(testTags, DocnapTag.createDocnapTagCollection(tags));
    }
    
    private TestConverter<TestDocument, DocnapDocument> matchDocuments(ArrayList<TestDocument> testDocuments) {
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);

        Collection<Document> documents = documentRepository.fetchAll();
        
        return new TestConverter<TestDocument, DocnapDocument>(testDocuments, DocnapDocument.createDocnapTagCollection(documents));
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
