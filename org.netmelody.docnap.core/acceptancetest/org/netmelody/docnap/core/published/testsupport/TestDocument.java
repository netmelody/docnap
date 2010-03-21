package org.netmelody.docnap.core.published.testsupport;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TestDocument implements DocnapMatcher<DocnapDocument> {

    private final File file; 
    
    public TestDocument(File file) {
        this.file = file;
    }
    
    public boolean matchesDocnapInstance(DocnapDocument docnapDocument) throws IOException {
        return FileUtils.readFileToString(file).equals(FileUtils.readFileToString(docnapDocument.getFile()));
    }

    public File getFile() {
        return file;
    }
    
    
}
