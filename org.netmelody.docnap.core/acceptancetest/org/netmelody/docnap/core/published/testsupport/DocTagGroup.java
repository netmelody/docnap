package org.netmelody.docnap.core.published.testsupport;

import java.util.ArrayList;

public class DocTagGroup {
    
    final ArrayList<DocumentProperties> documents;
    final ArrayList<TagProperties> tags;
    
    public DocTagGroup(ArrayList<DocumentProperties> documents, ArrayList<TagProperties> tags) {
        this.documents = documents;
        this.tags = tags;
    }
    
}
