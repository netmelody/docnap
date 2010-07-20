package org.netmelody.docnap.core.testsupport.checker;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;


public class TestConverter <K extends IDocnapTestConverter<V>, V>{
    
 private final HashMap<K, V> conversionMap = new HashMap<K, V>();
    
    public TestConverter(Collection<K> testObjects, Collection<V> docnapObjects) throws IOException {
        for (K testObject : testObjects) {

            int count = 0;
            V matchedObject = null;
            for (V docnapObject : docnapObjects) {
                if (testObject.matchesDocnapInstance(docnapObject)) {
                    count++;
                    matchedObject = docnapObject;
                }
            }
            assertEquals("Incorrect number of found ", 1, count);
            addLink(testObject, matchedObject);
        }
    }
    
    
    public void addLink(K testObject, V docnapObject) {
        conversionMap.put(testObject, docnapObject);
    }
    
    public V getDocnapVersion(K testObject) {
        return conversionMap.get(testObject);
    }
    
    public void checkMappingsEqual(IDocnapObjectChecker<K, V> checker) throws IOException {
        for (Entry<K, V> entryToCheck : conversionMap.entrySet()) {
            checker.equalsDocnapInstance(entryToCheck.getKey(), entryToCheck.getValue());
            checker.hasCorrectLinks(entryToCheck.getKey(), entryToCheck.getValue());
        }
    }

}
