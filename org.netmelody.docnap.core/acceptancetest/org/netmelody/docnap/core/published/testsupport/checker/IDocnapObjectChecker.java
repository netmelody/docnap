package org.netmelody.docnap.core.published.testsupport.checker;

import java.io.IOException;

public interface IDocnapObjectChecker <K, V> {
    
    public void equalsDocnapInstance(K testObject, V docnapObject) throws IOException;
    
    public void hasCorrectLinks(K testObject, V docnapObject);

}
