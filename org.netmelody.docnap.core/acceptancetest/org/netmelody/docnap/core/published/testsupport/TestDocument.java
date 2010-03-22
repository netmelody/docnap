package org.netmelody.docnap.core.published.testsupport;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TestDocument implements DocnapMatcher<DocnapDocument> {

    private final File file; 
    private final String title;
    private final String originalFilename;
    
    public TestDocument(File file) {
        this(file, null);
    }

    public TestDocument(File file, String title) {
        this(file, title, null);
    }
    
    public TestDocument(File file, String title, String originalFilename) {
        this.file = file;
        this.title = title;
        this.originalFilename = originalFilename;
    }
    
    public boolean matchesDocnapInstance(DocnapDocument docnapDocument) throws IOException {
        return FileUtils.readFileToString(file).equals(FileUtils.readFileToString(docnapDocument.getFile()));
    }

    public File getFile() {
        return file;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getOriginalFilename() {
        return originalFilename;
    }
    
    
}
