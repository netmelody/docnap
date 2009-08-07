package org.netmelody.docnap.core.published;

import java.io.File;
import java.util.Collection;

import org.netmelody.docnap.core.domain.Document;

public interface IDocumentRepository {

	Document addDocument(File documentFile);

	Collection<Document> fetchAll();
}