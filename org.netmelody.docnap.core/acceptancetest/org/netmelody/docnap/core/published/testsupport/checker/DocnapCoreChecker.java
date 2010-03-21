package org.netmelody.docnap.core.published.testsupport.checker;

import static org.junit.Assert.assertEquals;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.published.testsupport.DocnapStoreTestGroup;
import org.netmelody.docnap.core.published.testsupport.DocnapTagProperties;
import org.netmelody.docnap.core.published.testsupport.TagProperties;
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
    }
    
    private void matchTags(ArrayList<TagProperties> testTags) {
        ITagRepository tagRepository = context.getComponent(ITagRepository.class);

        Collection<Tag> tags = tagRepository.fetchAll();
        
        for (Tag tag : tags) {
            DocnapTagProperties docnapTag = new DocnapTagProperties(tag);
            
            int count = 0;
            TagProperties matchedTestTag;
            for (TagProperties tagTest : testTags) {
                if (tagTest.matchesDocnapTag(docnapTag))
                {
                    count++;
                    matchedTestTag = tagTest;
                }
            }
            assertEquals("Incorrect number of tags found " + tag.getTitle(), 1, count);
        }
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
