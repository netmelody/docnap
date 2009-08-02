package org.netmelody.docnap.core.repository;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.netmelody.docnap.core.exception.DocnapRuntimeException;
import org.netmelody.docnap.core.published.IDocumentRepository;

public class DocumentRepository implements IDocumentRepository {

	private static final String DIRNAME_DOCS = "docs";
	
	private final IDocnapStoreConnection connection;

	public DocumentRepository(IDocnapStoreConnection connection) {
		this.connection = connection;
	}
	
	public void addDocument(File documentFile) {
		final File storageLocation = new File(this.connection.getStorageLocation(), DIRNAME_DOCS);
		final String dirName = String.format("%03d", Math.round(Math.random()*100.0));
		final String fileName = UUID.randomUUID().toString();
		final File destination = new File(new File(storageLocation, dirName), fileName);

		try {
			FileUtils.copyFile(documentFile, destination, true);
		}
		catch (IOException exception) {
			throw new DocnapRuntimeException("Failed to add document.", exception);
		}
		
		final String sqlText = "INSERT INTO DOCUMENTS (handle, original_filename) VALUES ('"+ dirName + "." + fileName+"', '"+documentFile.getName()+"');";
		this.connection.executeStatement(sqlText);
	}
}
