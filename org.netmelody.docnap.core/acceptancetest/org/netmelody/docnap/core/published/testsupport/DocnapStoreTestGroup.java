package org.netmelody.docnap.core.published.testsupport;

import java.util.ArrayList;

public class DocnapStoreTestGroup {
    
    private final ArrayList<TestDocument> documents = new ArrayList<TestDocument>();
    private final ArrayList<TestTag> tags = new ArrayList<TestTag>();
    
    public DocnapStoreTestGroup() {
        
    }
    
    public DocnapStoreTestGroup(TestDocument document) {
        documents.add(document);
    }
    
    public DocnapStoreTestGroup(TestDocument document, TestTag tag) {
        this(document);
        
        tags.add(tag);
    }
    
    public DocnapStoreTestGroup(ArrayList<TestDocument> testDocuments) {
        documents.addAll(testDocuments);
    }

    public void addTag(TestTag tag) {
        tags.add(tag);
    }
    
    public void addDocument(TestDocument document) {
        documents.add(document);
    }
    
    public void addLink(DocumentStore document, TestTag tag) {
        
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
