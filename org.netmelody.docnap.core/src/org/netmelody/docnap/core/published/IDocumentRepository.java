package org.netmelody.docnap.core.published;

import java.io.File;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Document;

public interface IDocumentRepository {

    Document addFile(File documentFile);

    void retrieveFile(Document document, File destination);

    void retrieveAllFilesAsZip(File outFile);

    Document save(Document document);
    
    Collection<Document> fetchAll();

    Collection<Document> findByTagId(Integer identity);
    
    int getNumberOfDocuments();
    
    void removeDocument(Document document);
    
}