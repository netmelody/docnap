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
import org.netmelody.docnap.core.type.DocnapDateTime;

public class DocumentRepository implements IDocumentRepository {

    private static final String DIRNAME_DOCS = "docs";
    
    private final IDocnapStoreConnection connection;
    
    private final DocnapInsertStatement insertDocumentStatement;
    private final DocnapSelectStatement retrieveDocumentStatement;
    private final DocnapDmlStatement updateDocumentStatement;
    private final DocnapSelectStatement fetchByIdStatement;
    private final DocnapSelectStatement fetchAllStatement;
    private final DocnapSelectStatement findByTagIdStatement;
    private final DocnapDmlStatement deleteDocumentTagLinksStatement;
    private final DocnapDmlStatement deleteDocumentStatement;
    
    private static final String INSERT_DOCUMENT_EXPRESSION = 
    	 "INSERT INTO DOCUMENTS (handle, original_filename) VALUES" +
         " (?, ?);";
    
    private static final String RETRIEVE_DOCUMENT_EXPRESSION = 
    	"SELECT handle FROM DOCUMENTS WHERE documentid = ?";
    
    private static final String UPDATE_DOCUMENT_EXPRESSION = 
    	"UPDATE DOCUMENTS SET title = ? WHERE documentid = ?";
    
    private static final String FETCH_BY_ID_EXPRESSION = 
    	"SELECT documentid, handle, title, original_filename, checkin_dt " +
    	"  FROM DOCUMENTS WHERE documentid = ?";
    
    private static final String FETCH_ALL_EXPRESSION = 
    	"SELECT documentid, handle, title, original_filename, checkin_dt FROM DOCUMENTS";
    
    private static final String FIND_BY_TAG_ID_EXPRESSION = 
    	"SELECT documentid, handle, title, original_filename, checkin_dt" +
        "  FROM DOCUMENTS d INNER JOIN DOCUMENTTAGLINKS l" +
        "    ON (d.documentid = l.documentid)" +
        " WHERE l.tagid = ?";
    
    private static final String DELETE_DOCUMENT_TAG_LINKS_EXPRESSION = 
    	"DELETE FROM DOCUMENTTAGLINKS WHERE documentid = ?";
    
    private static final String DELETE_DOCUMENT_EXPRESSION = 
    	"DELTET FROM DOCUMENTS WHERE documentid = ?";

    public DocumentRepository(IDocnapStoreConnection connection) {
        this.connection = connection;
        
        insertDocumentStatement = new DocnapInsertStatement(this.connection, INSERT_DOCUMENT_EXPRESSION);
        retrieveDocumentStatement = new DocnapSelectStatement(this.connection, RETRIEVE_DOCUMENT_EXPRESSION);
        updateDocumentStatement = new DocnapDmlStatement(this.connection, UPDATE_DOCUMENT_EXPRESSION);
        fetchByIdStatement = new DocnapSelectStatement(this.connection, FETCH_BY_ID_EXPRESSION);
        fetchAllStatement = new DocnapSelectStatement(this.connection, FETCH_ALL_EXPRESSION);
        findByTagIdStatement = new DocnapSelectStatement(this.connection, FIND_BY_TAG_ID_EXPRESSION);
        deleteDocumentTagLinksStatement = new DocnapDmlStatement(this.connection, DELETE_DOCUMENT_TAG_LINKS_EXPRESSION);
        deleteDocumentStatement = new DocnapDmlStatement(this.connection, DELETE_DOCUMENT_EXPRESSION);
    }
    
    public Document addDocument(File documentFile) {
        final File storageLocation = new File(this.connection.getStorageLocation(), DIRNAME_DOCS);
        final String dirName = String.format("%03d", Math.round(Math.random()*100.0));
        final String fileName = UUID.randomUUID().toString();
        final String documentName = documentFile.getName();
        int extIndex = documentName.lastIndexOf('.');
        final String extension = (-1 == extIndex) ? "" : documentName.substring(extIndex);
        final File destination = new File(new File(storageLocation, dirName), fileName + extension);

        try {
            FileUtils.copyFile(documentFile, destination, true);
        }
        catch (IOException exception) {
            throw new DocnapRuntimeException("Failed to add document.", exception);
        }
        
        final Integer identity = insertDocumentStatement.execute(new Object[] {dirName + "." + fileName + extension, documentName});
        
        return fetchById(identity);
    }
    
    public void removeDocument(Document document) {
    	Integer identity = document.getIdentity();
    	String handle = getDocumentHandle(document);
    	
    	deleteDocumentTagLinksStatement.execute(new Object[] {identity});
    	Integer deleteCount = deleteDocumentStatement.execute(new Object[] {identity});
    	
    	if (deleteCount != 1) {
    		throw new DocnapRuntimeException("Failed to delete document " + identity);
    	}
    	
    	 final File storageLocation = new File(this.connection.getStorageLocation(), DIRNAME_DOCS);    

         final int separatorIndex = handle.indexOf('.');
         final String dirName = handle.substring(0, separatorIndex);
         final String fileName = handle.substring(separatorIndex+1);
 		 File docFile = new File(new File(storageLocation, dirName), fileName);
    	 
    	 FileUtils.deleteQuietly(docFile);
    }
    
    private String getDocumentHandle(Document document) {
    	final Integer identity = document.getIdentity();
        final ResultSet resultSet = retrieveDocumentStatement.execute(new Object[] {identity});
        
        final String handle;
        try {
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Invalid Document identifier");
            }
            handle = resultSet.getString("handle");
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to retrieve document with identifier: " + identity, exception);
        }
        
        return handle;
    }
    
    public void retrieveDocument(Document document, File outFile) {
        String handle = getDocumentHandle(document);
        
        int separatorIndex = handle.indexOf('.');
        final String dirName = handle.substring(0, separatorIndex);
        final String fileName = handle.substring(separatorIndex+1);
        
        final File storageLocation = new File(this.connection.getStorageLocation(), DIRNAME_DOCS);
        final File storedFile = new File(new File(storageLocation, dirName), fileName);
        
        try {
            FileUtils.copyFile(storedFile, outFile, true);
        }
        catch (IOException exception) {
            throw new DocnapRuntimeException("Failed to retrieve document.", exception);
        }
    }
    
    public Document save(Document document) {
        updateDocumentStatement.execute(new Object[] {document.getTitle(), document.getIdentity()});
        return fetchById(document.getIdentity());
    }
    
    public Document fetchById(Integer identity) {
        final ResultSet resultSet = fetchByIdStatement.execute(new Object[] {identity});
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
        return fetchMultipleWithSql(fetchAllStatement, null);
    }

    public Collection<Document> findByTagId(Integer tagId) {
        return fetchMultipleWithSql(findByTagIdStatement, new Object[] {tagId});
    }
    
    public void saveAllDocumentsToZip(File outFile) {
        final ResultSet resultSet = fetchAllStatement.execute(null);
              
        final File storageLocation = new File(this.connection.getStorageLocation(), DIRNAME_DOCS);
        
        DocZipOutput docZip;
        try {
        	docZip= new DocZipOutput(outFile);
        }
        catch (IOException exception) {
        	throw new DocnapRuntimeException("Failed to create DocZip", exception);
        }
        
        try {            
            while(resultSet.next()) {
              final String handle = resultSet.getString("handle");
              final String originalFilename = resultSet.getString("original_filename");
              final int separatorIndex = handle.indexOf('.');
              final String dirName = handle.substring(0, separatorIndex);
              final String fileName = handle.substring(separatorIndex+1);
      		  File document = new File(new File(storageLocation, dirName), fileName);
              
              docZip.addDocument(document, originalFilename);    
            }
            
            docZip.close();
            
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to retrieve all documents: ", exception);
        }
        catch (IOException exception) {
        	throw new DocnapRuntimeException("Failed to save files to zip", exception);
        }
        
        
    }
    
    private Collection<Document> fetchMultipleWithSql(DocnapSelectStatement statement, Object[] args) {
	    final ResultSet resultSet = statement.execute(args);
	    
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
        doc.setDateAdded(new DocnapDateTime(resultSet.getTimestamp("checkin_dt")));
        return doc;
    }
	
	public int getCount() {
		final String sqlStmt = "SELECT COUNT(*) documentCount " +
		                       "  FROM DOCUMENTS";
		final ResultSet resultSet = this.connection.executeSelect(sqlStmt);
		int documentCount = 0;
		try {
			if (resultSet.next()) {
				documentCount = resultSet.getInt("documentCount");
			}
			else {
				throw new DocnapRuntimeException("No rows returned for document count", null);
			}
		}
		catch (SQLException exception) {
			throw new DocnapRuntimeException("Failed to get count of documents", exception);
		}
		return documentCount;
	}
}
