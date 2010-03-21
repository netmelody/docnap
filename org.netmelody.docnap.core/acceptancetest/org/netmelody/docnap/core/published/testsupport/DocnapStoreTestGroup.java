package org.netmelody.docnap.core.published.testsupport;

import java.util.ArrayList;

public class DocnapStoreTestGroup {
    
    private ArrayList<DocumentProperties> documents;
    private ArrayList<TagProperties> tags;

    public void addTag(TagProperties tag) {
        tags.add(tag);
    }
    
    public void addDocument(DocumentProperties document) {
        documents.add(document);
    }
    
    public void addLink(DocumentProperties document, TagProperties tag) {
        
    }
    
    public ArrayList<TagProperties> getTags() {
        return tags;
    }
    
    public ArrayList<DocumentProperties> getDocuments() {
        return documents;
    }
    
    public int getNumberOfTags() {
        return tags.size();
    }
    
    public int getNumberOfDocuments() {
        return documents.size();
    }
    
}
