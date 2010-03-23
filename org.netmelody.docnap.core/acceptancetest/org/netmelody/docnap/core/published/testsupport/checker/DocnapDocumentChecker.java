package org.netmelody.docnap.core.published.testsupport.checker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.netmelody.docnap.core.testsupport.utilities.AssertUtility.assertEqualsWithNull;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.netmelody.docnap.core.published.testsuport.domain.DocnapDocument;
import org.netmelody.docnap.core.published.testsuport.domain.TestDocument;

public class DocnapDocumentChecker {

    public static void checkThatTheFileRetrievedIsCorrect(TestDocument testDocument, File retrievedFile) throws IOException {
        assertThat("Incorrect file content.", FileUtils.readFileToString(retrievedFile), is(FileUtils.readFileToString(testDocument.getFile())));
    }
    
    public static void checkDocumentProperties(TestDocument testDocument, DocnapDocument docnapDocument) throws IOException{
        checkThatTheFileRetrievedIsCorrect(testDocument, docnapDocument.getFile());
        
        assertEqualsWithNull("Document title not correct", testDocument.getTitle(), docnapDocument.getDocument().getTitle());
        assertEqualsWithNull("Document original filename not correct", testDocument.getOriginalFilename(), docnapDocument.getDocument().getOriginalFilename());
    }
    
    public void equalsDocnapInstance(TestDocument testDocument, DocnapDocument docnapDocument) throws IOException {
        checkThatTheFileRetrievedIsCorrect(testDocument, docnapDocument.getFile());
        
        assertEqualsWithNull("Document title not correct", testDocument.getTitle(), docnapDocument.getDocument().getTitle());
        assertEqualsWithNull("Document original filename not correct", testDocument.getOriginalFilename(), docnapDocument.getDocument().getOriginalFilename());
    
    }
}
