package org.netmelody.docnap.core.domain;

import org.netmelody.docnap.core.type.DocnapDateTime;

public final class Document extends DocnapObject {

    public static final String PROPERTYNAME_IDENTITY = "identity";
    public static final String PROPERTYNAME_TITLE = "title";
    public static final String PROPERTYNAME_DATEADDED = "dateAdded";
    public static final String PROPERTYNAME_DATERECEIVED = "dateReceived";
    public static final String PROPERTYNAME_DATESENT = "dateSent";
    public static final String PROPERTYNAME_ORIGINALFILENAME = "originalFilename";
    
    private final Integer identity;
    private final String handle;

    private String title;
    private DocnapDateTime dateAdded;
    private DocnapDateTime dateSent;
    private DocnapDateTime dateReceived;
    private String originalFilename;

    public Document() {
        this(null, null);
        this.dateReceived = new DocnapDateTime();
    }

    public Document(Integer identity, String handle) {
        this.identity = identity;
        this.handle = handle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DocnapDateTime getDateAdded() {
        return dateAdded;
    }
    
    public void setDateAdded(DocnapDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }
   
    public DocnapDateTime getDateReceived() {
        return this.dateReceived;
    }

    public void setDateReceived(DocnapDateTime dateReceived) {
        this.dateReceived = dateReceived;
    }
    
    public DocnapDateTime getDateSent() {
        return this.dateSent;
    }
    
    public void setDateSent(DocnapDateTime dateSent) {
        this.dateSent = dateSent;
    }
    
    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }
    
    @Override
    public String toString() {
        return (null == this.title) ? String.valueOf(this.identity) : this.title;
    }

    public Integer getIdentity() {
        return this.identity;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (!(obj instanceof Document)) {
            return false;
        }
        
        if (this.identity != ((Document)obj).identity) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public int hashCode() {
        return 17 * 31 + identity;
    }
}
