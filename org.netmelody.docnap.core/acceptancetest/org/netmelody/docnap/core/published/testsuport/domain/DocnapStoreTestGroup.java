package org.netmelody.docnap.core.published.testsuport.domain;

import java.util.ArrayList;
import org.netmelody.docnap.core.published.testsupport.DocumentStore;

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
        this(testDocuments, null);
    }
    
    public DocnapStoreTestGroup(ArrayList<TestDocument> testDocuments, ArrayList<TestTag> testTags) {
        documents.addAll(testDocuments);
        tags.addAll(testTags);
    }

    public void addTag(TestTag tag) {
        tags.add(tag);
    }
    
    public void addDocument(TestDocument document) {
        documents.add(document);
    }
    
    public void addLink(DocumentStore document, TestTag tag) {
        
    }
    
    public void getTagsForDocument(TestDocument testDocument) {
        
    }
    
    public void getDocumentsForTag(TestTag testTag) {
        
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
