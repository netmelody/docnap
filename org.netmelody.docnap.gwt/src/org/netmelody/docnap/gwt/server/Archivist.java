package org.netmelody.docnap.gwt.server;

import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;

public class Archivist {

    public static final String NAME = "archivist";

    private IDocnapStore docnapStore;
    private IDocumentRepository documentRepository;
    private ITagRepository tagRepository;
    
    public Archivist(IDocnapStore docnapStore, IDocumentRepository documentRepository, ITagRepository tagRepository) {
        super();
        this.docnapStore = docnapStore;
        this.documentRepository = documentRepository;
        this.tagRepository = tagRepository;
    }

}
