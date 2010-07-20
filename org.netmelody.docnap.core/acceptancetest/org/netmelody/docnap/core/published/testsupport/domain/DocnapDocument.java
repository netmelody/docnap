package org.netmelody.docnap.core.published.testsupport.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.testsupport.DocnapFactory;
import org.picocontainer.PicoContainer;

public class DocnapDocument {
    
    private final Document document;
    private final File file;
    
    public DocnapDocument(Document document, File file) {
        this.document = document;
        this.file = file;
    }

    public static Collection<DocnapDocument> createDocnapTagCollection(Collection<Document> documents, PicoContainer context, DocnapFactory docnapFactory) throws IOException{
        ArrayList<DocnapDocument> docnapDocumentList = new ArrayList<DocnapDocument>();
        IDocumentRepository documentRepository = context.getComponent(IDocumentRepository.class);
        
        for (Document document : documents) {
            File retrievedFile = docnapFactory.aNewEmptyFile();
            documentRepository.retrieveFile(document, retrievedFile);
            docnapDocumentList.add(new DocnapDocument(document, retrievedFile));
        }
 
        return docnapDocumentList;
    }
    
    public File getFile() {
        return file;
    }
    
    public Document getDocument() {
        return document;
    }
}
