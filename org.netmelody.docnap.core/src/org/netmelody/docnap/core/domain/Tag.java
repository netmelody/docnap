package org.netmelody.docnap.core.domain;

import org.netmelody.docnap.core.type.DocnapDateTime;

public class Tag extends DocnapObject {

    public static final String PROPERTYNAME_IDENTITY = "identity";
    public static final String PROPERTYNAME_TITLE = "title";
    public static final String PROPERTYNAME_DATECREATED = "dateCreated";
    public static final String PROPERTYNAME_DESCRIPTION = "description";

    private final Integer identity;

    private String title;
    private DocnapDateTime dateCreated;
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIdentity() {
        return identity;
    }

    public Tag(final Integer identity) {
        super();
        this.identity = identity;
    }

    public void setDateCreated(DocnapDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public DocnapDateTime getDateCreated() {
        return dateCreated;
    }
    
    @Override
    public String toString() {
        return this.title;
    }
}
