package org.netmelody.docnap.core.published.testsuport.domain;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DocnapStoreTestGroup {
    
    private final ArrayList<TestDocument> documents = new ArrayList<TestDocument>();
    private final ArrayList<TestTag> tags = new ArrayList<TestTag>();
    
    private final HashMap<TestDocument, ArrayList<TestTag>> documentLinks = new HashMap<TestDocument, ArrayList<TestTag>>();
    private final HashMap<TestTag, ArrayList<TestDocument>> tagLinks = new HashMap<TestTag, ArrayList<TestDocument>>();
    
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
    
    public void addLink(TestDocument document, TestTag tag) {
        assertTrue(documents.contains(document));
        assertTrue(tags.contains(tag));
        
        addLinkToMap(documentLinks, document, tag);
        addLinkToMap(tagLinks, tag, document);
    }
    
    private <K, V> void addLinkToMap(HashMap<K, ArrayList<V>> linkMap, K fromObject, V toObject) {
        if (linkMap.containsKey(fromObject)) {
            linkMap.get(fromObject).add(toObject);
        }
        else {
            ArrayList<V> newList = new ArrayList<V>();
            newList.add(toObject);
            linkMap.put(fromObject, newList);
        }
    }
    
    public Collection<TestTag> getTagsForDocument(TestDocument testDocument) {
        return getLinksForObject(documentLinks, testDocument);
    }
    
    public Collection<TestDocument> getDocumentsForTag(TestTag testTag) {
        return getLinksForObject(tagLinks, testTag);
    }
    
    private <K, V> Collection<V> getLinksForObject(HashMap<K, ArrayList<V>> linkMap, K object) {
        if (linkMap.containsKey(object)) {
            return linkMap.get(object);
        }
        else {
            return new ArrayList<V>();
        }
    }
    
    public Collection<TestTag> getTags() {
        return tags;
    }
    
    public Collection<TestDocument> getDocuments() {
        return documents;
    }
    
    public int getNumberOfTags() {
        return tags.size();
    }
    
    public int getNumberOfDocuments() {
        return documents.size();
    }
    
}
