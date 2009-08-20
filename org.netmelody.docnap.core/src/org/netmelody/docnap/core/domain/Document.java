package org.netmelody.docnap.core.domain;

import org.netmelody.docnap.core.type.DocnapDateTime;

public final class Document extends DocnapObject {

	public static final String PROPERTYNAME_TITLE = "title";
	
	private final Integer identity;
	private final String handle;

	private String title;
	private DocnapDateTime dateAdded;
	private String originalFilename;

	public Document() {
		this(null, null);
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
}
