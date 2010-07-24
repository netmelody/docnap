package org.netmelody.docnap.core.testsupport.checker;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;
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

    public void hasOneDocumentContaining(File file) {
        hasTheCorrectNumberOfDocuments(1);
        Collection<Document> documents = docnapCore.fetchAllDocuments();
        final Document document = documents.iterator().next();
        assertThat(document.getOriginalFilename(), is(equalTo(file.getName())));
        
        final File documentFile = docnapCore.fetchFileFor(document);
        assertThat(documentFile, hasContentsEqualTo(file));
    }

    private Matcher<File> hasContentsEqualTo(File file) {
        return new FileContentsMatcher(file);
    }

    private static class FileContentsMatcher extends TypeSafeMatcher<File> {

        private final File target;

        public FileContentsMatcher(File target) {
            this.target = target;
        }

        @Override
        public void describeTo(Description desc) {
            desc.appendText(" a file with contents matching " + target.getName());
        }

        @Override
        public boolean matchesSafely(File item) {
            try {
                return FileUtils.contentEquals(target, item);
            }
            catch (IOException e) {
                return false;
            }
        }
    }
    
}
