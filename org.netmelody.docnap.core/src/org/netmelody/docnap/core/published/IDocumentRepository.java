package org.netmelody.docnap.core.published;

import java.io.File;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Document;

public interface IDocumentRepository {

    Document addDocument(File documentFile);

    void retrieveDocument(Document document, File destination);
    
    Document save(Document document);
    
    Collection<Document> fetchAll();

    Collection<Document> findByTagId(Integer identity);
}