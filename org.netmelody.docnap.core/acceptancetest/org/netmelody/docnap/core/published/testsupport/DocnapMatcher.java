package org.netmelody.docnap.core.published.testsupport;

import java.io.IOException;

public interface DocnapMatcher <V> {
    
    public boolean matchesDocnapInstance(V object) throws IOException;

}
