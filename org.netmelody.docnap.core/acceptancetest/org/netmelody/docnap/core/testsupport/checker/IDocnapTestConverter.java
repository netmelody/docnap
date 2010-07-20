package org.netmelody.docnap.core.testsupport.checker;

import java.io.IOException;

public interface IDocnapTestConverter <V> {
    
    public boolean matchesDocnapInstance(V object) throws IOException;
    
}
