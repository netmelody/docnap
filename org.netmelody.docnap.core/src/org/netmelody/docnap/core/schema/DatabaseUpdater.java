package org.netmelody.docnap.core.schema;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.netmelody.docnap.core.exception.DocnapRuntimeException;

import com.dbdeploy.DbDeploy;

public class DatabaseUpdater {

	private final Connection connection;
	private final DbDeploy dbDeploy = new DbDeploy();
	
	/**
     * Create a new database updater using the specified database connection.
     *
     * @param connection
     *     the database connection
     * @param password
     *     the connection user's password
     */
	public DatabaseUpdater(Connection connection, String password) {
		this.connection = connection;
		try {
			final DatabaseMetaData metaData = this.connection.getMetaData();
			this.dbDeploy.setDriver(DriverManager.getDriver(metaData.getURL()).getClass().getName());
			this.dbDeploy.setUrl(metaData.getURL()); 
			this.dbDeploy.setDbms(metaData.getDatabaseProductName().split(" ")[0].toLowerCase());
			this.dbDeploy.setUserid(metaData.getUserName());
		}
		catch (SQLException exception) {
			throw new DocnapRuntimeException("Invalid connection");
		}

		this.dbDeploy.setPassword(password);
	}

	public void updateDatabase() {
		createSchemaVersionTable();
		
		final File tempDirectory = createTempDirectory();
		this.dbDeploy.setScriptdirectory(tempDirectory);
		
		final File outputfile = new File(tempDirectory, "output.sql");
		this.dbDeploy.setOutputfile(outputfile);
		
		extractMigrationScripts(tempDirectory);
		
		try {
			this.dbDeploy.go();
			executeStatement(FileUtils.readFileToString(outputfile));
		}
		catch (Exception e) {
			throw new DocnapRuntimeException("Failed to upgrade database.", e);
		}
		finally {
			try {
				FileUtils.deleteDirectory(tempDirectory);
			} catch (IOException e) {
				throw new DocnapRuntimeException("Failed to delete temporary directory.", e);
			}
		}
	}

	private void createSchemaVersionTable() {
		int result = 0;
		try {
			if (this.connection.getMetaData().getTables(null, null, "%CHANGELOG%", null).first()) {
				return; // Schema version table already exists
			}

			final InputStream inputStream = getClass().getResourceAsStream("/scripts/createSchemaVersionTable.hsql.sql");
			final String expression = IOUtils.toString(inputStream);
			inputStream.close();
			
			result = executeStatement(expression);
		}
		catch (Exception exception) {
			result = -1;
		}

		if (result == -1) {
			throw new DocnapRuntimeException("Failed to create database version table");
		}
	}

	private int executeStatement(final String expression) throws SQLException {
		int result;
		final Statement statement = this.connection.createStatement();
		result = statement.executeUpdate(expression);
		statement.close();
		return result;
	}

	private void extractMigrationScripts(final File tempDirectory) {
		try {
			final InputStream inputStream = getClass().getResourceAsStream("/migrations/migrationindex.txt");
			final String[] migrationlist = IOUtils.toString(inputStream).split("\\r\\n");
			for (String migrationResourceName : migrationlist) {
				final InputStream migrationStream = getClass().getResourceAsStream("/migrations/" + migrationResourceName);
				final FileWriter outputWriter = new FileWriter(new File(tempDirectory, migrationResourceName));
				IOUtils.copy(migrationStream, outputWriter);
				outputWriter.close();
				migrationStream.close();
			}
			inputStream.close();
		} catch (IOException e1) {
			throw new DocnapRuntimeException("Failed to read database update scripts.");
		}
	}

	private static File createTempDirectory() {
		final File temp;
	
		try {
			temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
		}
		catch (IOException e) {
			throw new DocnapRuntimeException("Could not create temp file.");
		}
	
		if(!(temp.delete())) {
			throw new DocnapRuntimeException("Could not delete temp file: " + temp.getAbsolutePath());
		}
	
		if(!(temp.mkdir())) {
			throw new DocnapRuntimeException("Could not create temp directory: " + temp.getAbsolutePath());
		}
		
		temp.deleteOnExit();
		return (temp);
	}
}
