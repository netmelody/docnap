package org.netmelody.docnap.core.published.testsupport.checker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.netmelody.docnap.core.testsupport.utilities.AssertUtility.assertEqualsWithNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.published.testsuport.domain.DocnapDocument;
import org.netmelody.docnap.core.published.testsuport.domain.DocnapStoreTestGroup;
import org.netmelody.docnap.core.published.testsuport.domain.DocnapTag;
import org.netmelody.docnap.core.published.testsuport.domain.TestDocument;
import org.netmelody.docnap.core.published.testsuport.domain.TestTag;
import org.netmelody.docnap.core.testsupport.utilities.TagTitleComparator;
import org.netmelody.docnap.core.testsupport.utilities.TestTagTitleComparator;

public class DocnapDocumentChecker implements IDocnapObjectChecker<TestDocument, DocnapDocument>{

    private final ITagRepository tagRepository;
    private final DocnapStoreTestGroup group;
    private final TestConverter<TestTag, DocnapTag> tagConverter;
    
    public DocnapDocumentChecker(DocnapStoreTestGroup group, TestConverter<TestTag, DocnapTag> tagConverter, ITagRepository tagRepository) {
        this.tagRepository = tagRepository;
        this.group = group;
        this.tagConverter = tagConverter;
    }
    
    public static void checkThatTheFileRetrievedIsCorrect(TestDocument testDocument, File retrievedFile) throws IOException {
        assertThat("Incorrect file content.", FileUtils.readFileToString(retrievedFile), is(FileUtils.readFileToString(testDocument.getFile())));
    }
    
    public void equalsDocnapInstance(TestDocument testDocument, DocnapDocument docnapDocument) throws IOException {
        checkThatTheFileRetrievedIsCorrect(testDocument, docnapDocument.getFile());
        
        assertEqualsWithNull("Document title not correct", testDocument.getTitle(), docnapDocument.getDocument().getTitle());
        assertEqualsWithNull("Document original filename not correct", testDocument.getOriginalFilename(), docnapDocument.getDocument().getOriginalFilename());
    }
    
    public void hasCorrectLinks(TestDocument testDocument, DocnapDocument docnapDocument) {
        Collection<TestTag> testLinks = group.getTagsForDocument(testDocument);
        
        Collection<Tag> docnapLinks = tagRepository.findByDocumentId(docnapDocument.getDocument().getIdentity());
    
        assertEquals("Incorrect number of tags", testLinks.size(), docnapLinks.size());
        
        if (testLinks.size() == 0)
            return;
         
        Collections.sort((List<TestTag>) testLinks, new TestTagTitleComparator());
        Collections.sort((List<Tag>) docnapLinks, new TagTitleComparator());

        ArrayList<TestTag> testLinksList = (ArrayList<TestTag>) testLinks;
        ArrayList<Tag> docnapLinksList = (ArrayList<Tag>) docnapLinks;
        
        for (int tagIndex = 0; tagIndex < testLinksList.size(); tagIndex++) {
            assertEquals("Tag not correct", tagConverter.getDocnapVersion(testLinksList.get(tagIndex)).getTag(), docnapLinksList.get(tagIndex));
        }
    }
}
