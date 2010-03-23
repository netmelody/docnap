package org.netmelody.docnap.core.published.testsupport.checker;

import java.io.IOException;

public interface IDocnapTestConverter <V> {
    
    public boolean matchesDocnapInstance(V object) throws IOException;
    
    public void equalsDocnapInstance(V object) throws IOException;

}
