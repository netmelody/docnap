package org.netmelody.docnap.core.domain;

import java.io.File;

import org.netmelody.docnap.core.type.DocnapDateTime;

public final class Document extends DocnapObject {

	private final Integer identity;
	
	private File internalFileHandle;
	private String title;
	private DocnapDateTime dateAdded;
	
	public Document(Integer identity) {
		this.identity = identity;
	}
	
}
