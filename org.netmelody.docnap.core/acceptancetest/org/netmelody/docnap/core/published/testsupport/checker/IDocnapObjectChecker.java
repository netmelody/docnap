package org.netmelody.docnap.core.published.testsupport.checker;

import java.io.IOException;

public interface IDocnapObjectChecker {
    
    public void equalsDocnapInstance(Object object) throws IOException;
    
    public void hasCorrectLinks(Object object);

}
