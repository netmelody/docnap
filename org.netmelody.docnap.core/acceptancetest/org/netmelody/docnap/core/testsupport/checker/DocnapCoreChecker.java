package org.netmelody.docnap.core.testsupport.checker;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.netmelody.docnap.core.testsupport.matcher.FileContentsMatcher.hasContentsEqualTo;

import java.io.File;
import java.util.Collection;

import org.hamcrest.Matcher;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;

public class DocnapCoreChecker {
    
    private final DocnapCoreDriver docnapCore;
    private Document lastDocumentAccessed;
    
    public DocnapCoreChecker(DocnapCoreDriver docnapCore) {
        this.docnapCore = docnapCore;
    }

    private void hasTheCorrectNumberOfDocuments(int numberOfDocuments) {
        assertEquals("Incorrect number of documents in the store", numberOfDocuments, this.docnapCore.getNumberOfDocuments());
    }
    
    private void hasTheCorrectNumberOfTags(int numberOfTags) {
        assertEquals("Incorrect number of tags in the store", numberOfTags, this.docnapCore.getNumberOfTags());
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
        final Collection<Tag> tags = this.docnapCore.findTagByDocumentId(this.lastDocumentAccessed.getIdentity());
        
        assertThat(tags.size(), is(equalTo(1)));
        assertThat(tags.iterator().next().getTitle(), is(equalTo(tagTitle)));
    }

    public DocnapCoreChecker hasANumberOfDocumentsTagged(String tagTitle, Matcher<Integer> matcher) {
        final Tag tag = this.docnapCore.findTagByTitle(tagTitle);
        assertThat(tag.getDocumentCount(), matcher);
        return this;
    }
    
    public DocnapCoreChecker hasANumberOfDocumentsWithNoTag(Matcher<Integer> matcher) {
        final Collection<Document> docs = this.docnapCore.findUntaggedDocuments();
        assertThat(docs.size(), matcher);
        return this;
    }

    public DocnapCoreChecker and() {
        return this;
    }
}
