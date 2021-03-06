package org.netmelody.docnap.core.published;

import java.util.Collection;
import java.util.List;

import org.netmelody.docnap.core.domain.Tag;

public interface ITagRepository {

    Collection<Tag> findByDocumentId(Integer identity);
    
    Collection<Tag> findUnlinkedByDocumentId(Integer identity);
    
    List<Tag> fetchAll();
    
    Tag tagDocumentById(Integer documentId, String tagTitle);

    void unTagDocumentById(Integer documentId, String tagTitle);
    
    public void removeTag(Tag tag);

}