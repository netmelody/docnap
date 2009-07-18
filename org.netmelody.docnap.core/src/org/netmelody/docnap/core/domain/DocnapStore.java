package org.netmelody.docnap.core.domain;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.netmelody.docnap.core.exception.DocnapRuntimeException;

public class DocnapStore {

	private static final String DATABASE_CONNECTION_STRING_PREFIX = "jdbc:hsqldb:file:";
	private static final String DATABASE_NAME = "docnap";
	
	private Connection connection;
	
	public DocnapStore() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		}
		catch (ClassNotFoundException exception) {
			throw new DocnapRuntimeException("Failed to install HSQLDB JDBC driver.", exception);
		}
	}
	
	public void setStorageLocation(String path) {
		String databasePath;
		try {
			databasePath = new File(path, DATABASE_NAME).getCanonicalPath();
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
}
