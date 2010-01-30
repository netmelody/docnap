package org.netmelody.docnap.swingclient;

import org.netmelody.docnap.core.domain.Tag;

public class TagListEntry {
    
    private Tag tag;
    
    public TagListEntry(Tag tag) {
        this.tag = tag;
    }
        
    /**
     * @return the tag
     */
    public Tag getTag() {
        return tag;
    }

    @Override
    public String toString() {
        if (null != this.tag)
          return tag.toString() + " (" + tag.getDocumentCount() + ")";
        else
          return "";
    }

}
