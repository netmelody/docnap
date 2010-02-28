package org.netmelody.docnap.swingclient;

import org.netmelody.docnap.core.domain.Tag;

public class TagListEntry {
    
    private final Tag tag;
    
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
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (!(obj instanceof TagListEntry)) {
            return false;
        }
        
        return (this.tag.equals(((TagListEntry)obj).tag));
    }
    
    @Override
    public int hashCode() {
        return 17 * 31 + this.tag.hashCode();
    }

}
