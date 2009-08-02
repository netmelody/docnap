package org.netmelody.docnap.core.domain;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.netmelody.docnap.core.exception.DocnapRuntimeException;
import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.repository.IDocnapStoreConnection;
import org.netmelody.docnap.core.schema.DatabaseUpdater;

public class DocnapStore implements IDocnapStore {
	
	private IDocnapStoreConnection dsConnection;
	private DatabaseUpdater updater;
	
	public DocnapStore(final IDocnapStoreConnection dsConnection, DatabaseUpdater updater) {
		this.dsConnection = dsConnection;
		this.updater = updater;
	}
	
	//TODO: change to File
	public void setStorageLocation(String path) {
		this.dsConnection.setStorageLocation(new File(path));
		this.updater.updateDatabase();
	}

	//TODO: change to File
	public String getStorageLocation() {
		return this.dsConnection.getStorageLocation().getAbsolutePath();
	}
	
	public void addDocument(File documentFile) {
		final String storageLocation = getStorageLocation();
		final String dirName = String.valueOf(Math.round(Math.random()*100.0));
		final String fileName = UUID.randomUUID().toString();
		final File destination = new File(new File(storageLocation, dirName), fileName);

		try {
			FileUtils.copyFile(documentFile, destination, true);
		}
		catch (IOException exception) {
			throw new DocnapRuntimeException("Failed to add document.", exception);
		}
		
//		int result;
//		final Statement statement = this.connection.createStatement();
//		result = statement.executeUpdate("INSERT INTO DOCUMENTS (handle, original_filename) VALUES (#arg_handle, #arg_fn);");
//		statement.close();
//		return result;
	}
}
