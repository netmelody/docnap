package org.netmelody.docnap.core.repository;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.exception.DocnapRuntimeException;
import org.netmelody.docnap.core.published.IDocumentRepository;

public class DocumentRepository implements IDocumentRepository {

	private static final String DIRNAME_DOCS = "docs";
	
	private final IDocnapStoreConnection connection;

	public DocumentRepository(IDocnapStoreConnection connection) {
		this.connection = connection;
	}
	
	public Document addDocument(File documentFile) {
		final File storageLocation = new File(this.connection.getStorageLocation(), DIRNAME_DOCS);
		final String dirName = String.format("%03d", Math.round(Math.random()*100.0));
		final String fileName = UUID.randomUUID().toString();
		final String documentName = documentFile.getName();
		int extIndex = documentName.lastIndexOf(".");
		final String extension = (-1 == extIndex) ? "" : documentName.substring(extIndex);
		final File destination = new File(new File(storageLocation, dirName), fileName + extension);

		try {
			FileUtils.copyFile(documentFile, destination, true);
		}
		catch (IOException exception) {
			throw new DocnapRuntimeException("Failed to add document.", exception);
		}
		
		final String sqlText = "INSERT INTO DOCUMENTS (handle, original_filename) VALUES" +
				               " ('"+ dirName + "." + fileName + extension + "', '"+documentName+"');";
		final Integer identity = this.connection.executeInsert(sqlText);
		
		return fetchById(identity);
	}
	
	public Document save(Document document) {
		return document;
	}
	
	public Document fetchById(Integer identity) {
		final String sqlStmt = "SELECT documentid, handle, title, original_filename FROM DOCUMENTS WHERE documentid = " + identity;
		final ResultSet resultSet = this.connection.executeSelect(sqlStmt);
		try {
			if (!resultSet.next()) {
				throw new IllegalArgumentException("Invalid Document identifier");
			}
			return extractDocument(resultSet);
		}
		catch (SQLException exception) {
			throw new DocnapRuntimeException("Failed to retrieve document with identifier: " + identity, exception);
		}
	}
	
	public Collection<Document> findByExample(Document document) {
		return new ArrayList<Document>();
	}
	
	public Collection<Document> fetchAll() {
		final String sqlStmt = "SELECT documentid, handle, title, original_filename FROM DOCUMENTS";
		final ResultSet resultSet = this.connection.executeSelect(sqlStmt);
		
		final Collection<Document> result = new ArrayList<Document>();
		try {
			while(resultSet.next()) {
				result.add(extractDocument(resultSet));
			}
			resultSet.close();
		}
		catch (SQLException exception) {
			throw new DocnapRuntimeException("Failed to retrieve documents", exception);
		}
		return result;
	}

	private Document extractDocument(final ResultSet resultSet) throws SQLException {
		final Document doc = new Document(resultSet.getInt("documentid"), resultSet.getString("handle"));
		doc.setTitle(resultSet.getString("title"));
		doc.setOriginalFilename(resultSet.getString("original_filename"));
		return doc;
	}
}
