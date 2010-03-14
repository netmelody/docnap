package org.netmelody.docnap.core.published.testsupport;

import java.io.File;

import org.netmelody.docnap.core.domain.Document;

public class DocumentProperties {

    private final Document document;
    private final File file;
    
    public DocumentProperties(File file, Document document) {
        this.file = file;
        this.document = document;
    }
    
    public Document getDocument() {
        return document;
    }
    
    public File getFile() {
        return file;
    }
}
