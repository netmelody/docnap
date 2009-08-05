package org.netmelody.docnap.core.schema;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.netmelody.docnap.core.exception.DocnapRuntimeException;
import org.netmelody.docnap.core.repository.IDocnapStoreConnection;

import com.dbdeploy.DbDeploy;

public class DatabaseUpdater {

	private final IDocnapStoreConnection connection;
	private final DbDeploy dbDeploy = new DbDeploy();
	
	/**
     * Create a new database updater using the specified database connection.
     *
     * @param connection
     *     the database connection
     * @param password
     *     the connection user's password
     */
	public DatabaseUpdater(IDocnapStoreConnection connection) {
		this.connection = connection;
	}

	private void updateConnectionMetaData() {
		this.dbDeploy.setDriver(this.connection.getDriverClassName());
		this.dbDeploy.setUrl(this.connection.getDatabaseUrl()); 
		this.dbDeploy.setDbms(this.connection.getDatabaseProduct());
		this.dbDeploy.setUserid(this.connection.getUserId());
		this.dbDeploy.setPassword(this.connection.getPassword());
	}
	
	public void updateDatabase() {
		updateConnectionMetaData();
		createSchemaVersionTable();
		
		final File tempDirectory = createTempDirectory();
		this.dbDeploy.setScriptdirectory(tempDirectory);
		
		final File outputfile = new File(tempDirectory, "output.sql");
		this.dbDeploy.setOutputfile(outputfile);
		
		extractMigrationScripts(tempDirectory);
		
		try {
			this.dbDeploy.go();
			this.connection.executeDml(FileUtils.readFileToString(outputfile));
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
		try {
			if (this.connection.tableExists("CHANGELOG")) {
				return; // Schema version table already exists
			}

			final String versionTableScript = "/scripts/createSchemaVersionTable." + this.connection.getDatabaseProduct() + ".sql";
			final InputStream inputStream = getClass().getResourceAsStream(versionTableScript);
			final String expression = IOUtils.toString(inputStream);
			inputStream.close();
			
			this.connection.executeDml(expression);
		}
		catch (Exception exception) {
			throw new DocnapRuntimeException("Failed to create database version table");
		}
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
