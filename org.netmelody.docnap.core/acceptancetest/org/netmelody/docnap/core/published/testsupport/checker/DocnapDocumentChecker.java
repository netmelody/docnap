package org.netmelody.docnap.core.published.testsupport.checker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.netmelody.docnap.core.published.testsupport.DocumentStore;

public class DocnapDocumentChecker {

    public void checkThatTheFileRetrievedIsCorrect(DocumentStore document, File retrievedFile) throws IOException {
        assertThat("Incorrect file content.", FileUtils.readFileToString(retrievedFile), is(FileUtils.readFileToString(document.getFile())));
    }
}
