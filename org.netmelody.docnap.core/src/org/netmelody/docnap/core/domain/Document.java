package org.netmelody.docnap.core.domain;

import java.io.File;

import org.netmelody.docnap.core.type.DocnapDateTime;

class Document extends DocnapObject {

	private File internalFileHandle;
	
	private String title;
	private DocnapDateTime indexDate;
	
	
}
