package org.netmelody.docnap.core.published.testsupport;

import java.util.ArrayList;

public class DocnapStoreTestGroup {
    
    private ArrayList<TestDocument> documents;
    private ArrayList<TestTag> tags;

    public void addTag(TestTag tag) {
        tags.add(tag);
    }
    
    public void addDocument(TestDocument document) {
        documents.add(document);
    }
    
    public void addLink(DocumentProperties document, TestTag tag) {
        
    }
    
    public ArrayList<TestTag> getTags() {
        return tags;
    }
    
    public ArrayList<TestDocument> getDocuments() {
        return documents;
    }
    
    public int getNumberOfTags() {
        return tags.size();
    }
    
    public int getNumberOfDocuments() {
        return documents.size();
    }
    
}
