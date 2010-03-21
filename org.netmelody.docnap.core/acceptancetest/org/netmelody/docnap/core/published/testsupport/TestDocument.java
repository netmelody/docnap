package org.netmelody.docnap.core.published.testsupport;

import java.io.File;

public class TestDocument implements DocnapMatcher<DocnapDocument> {

    private final File file; 
    
    public TestDocument(File file) {
        this.file = file;
    }
    
    public boolean matchesDocnapInstance(DocnapDocument docnapDocument) {
        return false;
    }

    public File getFile() {
        return file;
    }
    
    
}
