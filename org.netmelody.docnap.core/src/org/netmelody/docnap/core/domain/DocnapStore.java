package org.netmelody.docnap.core.domain;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.netmelody.docnap.core.exception.DocnapRuntimeException;
import org.netmelody.docnap.core.schema.DatabaseUpdater;

public class DocnapStore {

	private static final String DRIVER = "org.hsqldb.jdbcDriver";
	private static final String DATABASE_CONNECTION_STRING_PREFIX = "jdbc:hsqldb:file:";
	private static final String DATABASE_NAME = "docnap";
	private static final String DIRNAME_DOCS = "docs";
	
	private Connection connection;
	
	public DocnapStore() {
		try {
			Class.forName(DRIVER);
		}
		catch (ClassNotFoundException exception) {
			throw new DocnapRuntimeException("Failed to install HSQLDB JDBC driver.", exception);
		}
	}
	
	private void shutDown() {
		if (null == this.connection) {
			return;
		}
		
		try {
	        final Statement statement = this.connection.createStatement();
	        statement.execute("SHUTDOWN");
	        this.connection.close();

		}
		catch (SQLException e) {
			throw new DocnapRuntimeException("Failed to shut down existing connection");
		}
	}
	
	//TODO: change to File
	public void setStorageLocation(String path) {
		shutDown();
		
		String databasePath;
		try {
			databasePath = new File(path, DATABASE_NAME).getCanonicalPath();
			final File file = new File(path, DIRNAME_DOCS);
			if (!file.exists()) {
				file.mkdir();
			}
		}
		catch (IOException exception) {
			throw new DocnapRuntimeException("Failed to calculate database path at [" + path + "].", exception);
		}
		
		try {
			this.connection = DriverManager.getConnection(DATABASE_CONNECTION_STRING_PREFIX + databasePath, "SA", "");
		}
		catch (SQLException exception) {
			throw new DocnapRuntimeException("Failed to open database at [" + path + "].", exception);
		}
		
		final DatabaseUpdater updater = new DatabaseUpdater(this.connection, "");
		updater.updateDatabase();
	}
	
	/**
	 * @return
	 */
	public String getStorageLocation() {
		try {
			final String url = this.connection.getMetaData().getURL();
			return url.substring(DATABASE_CONNECTION_STRING_PREFIX.length(), url.length() - DATABASE_NAME.length());
		}
		catch (Exception exception) {
			throw new DocnapRuntimeException("Failed to get storage location.", exception);
		}
	}
	
	public void addDocument(File documentFile) {
		final String storageLocation = getStorageLocation();
		final File destination = new File(new File(storageLocation, DIRNAME_DOCS), DIRNAME_DOCS + "1");
		
		try {
			FileUtils.copyFile(documentFile, destination, true);
		}
		catch (IOException exception) {
			throw new DocnapRuntimeException("Failed to add document.", exception);
		}
	}
}
