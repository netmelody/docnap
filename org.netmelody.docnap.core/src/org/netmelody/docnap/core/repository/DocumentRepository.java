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
    private final DocnapInsertStatement insertPageStatement;
    private final DocnapSelectStatement retrievePageStatement;
    private final DocnapDmlStatement updateDocumentStatement;
    private final DocnapSelectStatement fetchByIdStatement;
    private final DocnapSelectStatement fetchAllStatement;
    private final DocnapSelectStatement findByTagIdStatement;
    private final DocnapDmlStatement deleteDocumentTagLinksStatement;
    private final DocnapDmlStatement deleteDocumentStatement;
    private final DocnapSelectStatement getDocumentCountStatement;
    private final DocnapSelectStatement fetchAllOrderFilenameDescStatement;
    
    private static final String INSERT_DOCUMENT_EXPRESSION = 
    	 "INSERT INTO documents (title)" +
    	 "VALUES (null)";
    
    private static final String INSERT_PAGE_EXPRESSION = 
        "INSERT INTO pages (documentid, number, handle, original_filename) " +
        "VALUES (?, ?, ?, ?)";
    
    private static final String RETRIEVE_PAGE_EXPRESSION = 
    	"SELECT handle" +
    	"  FROM pages" +
    	" WHERE documentid = ? AND number = 1";
    
    private static final String UPDATE_DOCUMENT_EXPRESSION = 
    	"UPDATE documents" +
    	"   SET title = ?, sent_dt = ?, received_dt = ?" +
    	" WHERE documentid = ?";
    
    private static final String FETCH_BY_ID_EXPRESSION = 
    	"SELECT d.documentid, p.handle, d.title, p.original_filename," +
    	"       d.checkin_dt, d.sent_dt, d.received_dt" +
    	"  FROM documents d" +
    	"       INNER JOIN pages p ON (p.documentid = d.documentid)" +
    	" WHERE d.documentid = ? AND p.number = 1";
    
    private static final String FETCH_ALL_EXPRESSION = 
    	"SELECT d.documentid, p.handle, d.title, p.original_filename," +
    	"       d.checkin_dt, d.sent_dt, d.received_dt" +
    	"  FROM documents d" +
    	"       INNER JOIN pages p ON (p.documentid = d.documentid)" +
    	" WHERE p.number = 1";
    
    private static final String FIND_BY_TAG_ID_EXPRESSION = 
        "SELECT d.documentid, p.handle, d.title, p.original_filename," +
        "       d.checkin_dt, d.sent_dt, d.received_dt" +
        "  FROM documents d" +
        "       INNER JOIN documenttaglinks l ON (d.documentid = l.documentid)" +
        "       INNER JOIN pages p ON (p.documentid = d.documentid)" +
        " WHERE l.tagid = ? AND p.number = 1";
    
    private static final String FETCH_ORDERED_FILE_HANDLES_EXPRESSION = 
        "SELECT p.handle, p.original_filename" +
        "  FROM pages p" +
        " ORDER BY p.original_filename DESC, p.documentid ASC";
    
    private static final String DELETE_DOCUMENT_TAG_LINKS_EXPRESSION = 
    	"DELETE" +
    	"  FROM documenttaglinks" +
    	" WHERE documentid = ?";
    
    private static final String DELETE_DOCUMENT_EXPRESSION = 
    	"DELETE" +
    	"  FROM documents" +
    	" WHERE documentid = ?";
    
    private static final String GET_COUNT_EXPRESSION =
        "SELECT COUNT(*) documentCount " +
        "  FROM documents";

    public DocumentRepository(IDocnapStoreConnection connection) {
        this.connection = connection;
        
        insertDocumentStatement = new DocnapInsertStatement(this.connection, INSERT_DOCUMENT_EXPRESSION);
        insertPageStatement = new DocnapInsertStatement(this.connection, INSERT_PAGE_EXPRESSION);
        retrievePageStatement = new DocnapSelectStatement(this.connection, RETRIEVE_PAGE_EXPRESSION);
        updateDocumentStatement = new DocnapDmlStatement(this.connection, UPDATE_DOCUMENT_EXPRESSION);
        fetchByIdStatement = new DocnapSelectStatement(this.connection, FETCH_BY_ID_EXPRESSION);
        fetchAllStatement = new DocnapSelectStatement(this.connection, FETCH_ALL_EXPRESSION);
        findByTagIdStatement = new DocnapSelectStatement(this.connection, FIND_BY_TAG_ID_EXPRESSION);
        deleteDocumentTagLinksStatement = new DocnapDmlStatement(this.connection, DELETE_DOCUMENT_TAG_LINKS_EXPRESSION);
        deleteDocumentStatement = new DocnapDmlStatement(this.connection, DELETE_DOCUMENT_EXPRESSION);
        getDocumentCountStatement = new DocnapSelectStatement(this.connection, GET_COUNT_EXPRESSION);
        fetchAllOrderFilenameDescStatement = new DocnapSelectStatement(this.connection, FETCH_ORDERED_FILE_HANDLES_EXPRESSION);
    }
    
    public Document addFile(File documentFile) {
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
        
        final Integer identity = insertDocumentStatement.execute(new Object[0]);
        insertPageStatement.execute(new Object[] {identity, 1, dirName + "." + fileName + extension, documentName});
        
        return fetchById(identity);
    }
    
    public void removeDocument(Document document) {
    	Integer identity = document.getIdentity();
    	String handle = getPageHandle(document);
    	
    	deleteDocumentTagLinksStatement.execute(new Object[] {identity});
    	Integer deleteCount = deleteDocumentStatement.execute(new Object[] {identity});
    	
    	if (deleteCount != 1) {
    		throw new DocnapRuntimeException("Failed to delete document " + identity);
    	}
    	
    	final File docFile = convertHandleToString(handle);
    	 
    	 FileUtils.deleteQuietly(docFile);
    }
    
    public void retrieveFile(Document document, File outFile) {
        String handle = getPageHandle(document);      
        final File storedFile = convertHandleToString(handle);
        
        try {
            FileUtils.copyFile(storedFile, outFile, true);
        }
        catch (IOException exception) {
            throw new DocnapRuntimeException("Failed to retrieve document.", exception);
        }
    }
    
    public Document save(Document document) {
        updateDocumentStatement.execute(new Object[] {document.getTitle(),
                                                      DocnapDateTime.toTimestamp(document.getDateSent()),
                                                      DocnapDateTime.toTimestamp(document.getDateReceived()),
                                                      document.getIdentity()});
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
    
    public void retrieveAllFilesAsZip(File outFile) {
        final ResultSet resultSet = fetchAllOrderFilenameDescStatement.execute(null);
              
        try { 
            if (!resultSet.next())
                return;
            
            final File storageLocation = new File(this.connection.getStorageLocation(), DIRNAME_DOCS);
            DocZipOutput docZip;
            try {
                docZip= new DocZipOutput(outFile);
            }
            catch (IOException exception) {
                throw new DocnapRuntimeException("Failed to create DocZip", exception);
            }
                   
            do {
              final String handle = resultSet.getString("handle");
              File document = convertHandleToString(handle, storageLocation);
              final String originalFilename = resultSet.getString("original_filename");
              
              docZip.addDocument(document, originalFilename);  
            }
            while(resultSet.next());
            
            docZip.close();  
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to retrieve all documents: ", exception);
        }
        catch (IOException exception) {
        	throw new DocnapRuntimeException("Failed to save files to zip", exception);
        }  
    }
    
    public Integer getNumberOfDocuments() {
        final ResultSet resultSet = getDocumentCountStatement.execute(null);
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
        doc.setDateAdded(DocnapDateTime.fromTimestamp(resultSet.getTimestamp("checkin_dt")));
        doc.setDateSent(DocnapDateTime.fromTimestamp(resultSet.getTimestamp("sent_dt")));
        doc.setDateReceived(DocnapDateTime.fromTimestamp(resultSet.getTimestamp("received_dt")));
        return doc;
    }
	
	private String getPageHandle(Document document) {
        final Integer identity = document.getIdentity();
        final ResultSet resultSet = retrievePageStatement.execute(new Object[] {identity});
        
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
    
	private File convertHandleToString(String handle) {
	    final File storageLocation = new File(this.connection.getStorageLocation(), DIRNAME_DOCS);
        return convertHandleToString(handle, storageLocation);
	}
	
    private File convertHandleToString(String handle, File storageLocation) {
        int separatorIndex = handle.indexOf('.');
        final String dirName = handle.substring(0, separatorIndex);
        final String fileName = handle.substring(separatorIndex+1);
        
        final File storedFile = new File(new File(storageLocation, dirName), fileName);
        
        return storedFile;
    }
}
