package org.netmelody.docnap.core.domain;

import org.netmelody.docnap.core.type.DocnapDateTime;

public class Tag extends DocnapObject {

    public static final String PROPERTYNAME_IDENTITY = "identity";
    public static final String PROPERTYNAME_TITLE = "title";
    public static final String PROPERTYNAME_DATECREATED = "dateCreated";
    public static final String PROPERTYNAME_DESCRIPTION = "description";
    public static final String PROPERTYNAME_DOCUMENT_COUNT = "documentCount";

    private final Integer identity;

    private String title;
    private DocnapDateTime dateCreated;
    private String description;
    private int documentCount;

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
        
    /**
	 * @return the documentCount
	 */
	public int getDocumentCount() {
		return documentCount;
	}

	/**
	 * @param documentCount the documentCount to set
	 */
	public void setDocumentCount(int documentCount) {
		this.documentCount = documentCount;
	}

	@Override
    public String toString() {
		if (null != this.title)
          return this.title;
		else
		  return "";
    }
}
