package org.netmelody.docnap.core.published.testsupport;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;

public class TestConverter <K extends DocnapMatcher<V>, V>{
    
 private HashMap<K, V> tagMap = new HashMap<K, V>();
    
    public TestConverter(Collection<K> testObjects, Collection<V> docnapObjects) {
        for (K testObject : testObjects) {

            int count = 0;
            V matchedObject = null;
            for (V docnapObject : docnapObjects) {
                if (testObject.matchesDocnapInstance(docnapObject));
                {
                    count++;
                    matchedObject = docnapObject;
                }
            }
            assertEquals("Incorrect number of found ", 1, count);
            addLink(testObject, matchedObject);
        }
    }
    
    
    public void addLink(K testObject, V docnapObject) {
        tagMap.put(testObject, docnapObject);
    }

}
