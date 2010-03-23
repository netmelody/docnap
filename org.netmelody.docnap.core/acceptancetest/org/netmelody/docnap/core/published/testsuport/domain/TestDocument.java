package org.netmelody.docnap.core.published.testsuport.domain;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.netmelody.docnap.core.published.testsupport.checker.DocnapDocumentChecker;
import org.netmelody.docnap.core.published.testsupport.checker.IDocnapTestConverter;

public class TestDocument implements IDocnapTestConverter<DocnapDocument> {

    private final File file; 
    private final String title;
    private final String originalFilename;
    
    public TestDocument(File file) {
        this(file, null);
    }

    public TestDocument(File file, String title) {
        this.file = file;
        this.title = title;
        this.originalFilename = file.getName();
    }
    
    public boolean matchesDocnapInstance(DocnapDocument docnapDocument) throws IOException {
        return FileUtils.readFileToString(file).equals(FileUtils.readFileToString(docnapDocument.getFile()));
    }
    
    public void equalsDocnapInstance(DocnapDocument docnapDocument) throws IOException {
        DocnapDocumentChecker.checkDocumentProperties(this, docnapDocument);
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
