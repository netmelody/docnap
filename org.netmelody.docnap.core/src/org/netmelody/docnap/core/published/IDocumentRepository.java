package org.netmelody.docnap.core.published;

import java.io.File;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Document;

public interface IDocumentRepository {

    //TODO: rename to add file
    Document addDocument(File documentFile);

    //TODO: rename to retrieveFile
    void retrieveDocument(Document document, File destination);

    //TODO: rename to retrieveAllFilesAsZip
    void saveAllDocumentsToZip(File outFile);


    Document save(Document document);
    
    Collection<Document> fetchAll();

    Collection<Document> findByTagId(Integer identity);
    
    // TODO: rename to getNumberOfDocuments 
    int getCount();
    
}