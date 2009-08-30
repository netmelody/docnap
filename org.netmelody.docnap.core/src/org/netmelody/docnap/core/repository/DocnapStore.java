package org.netmelody.docnap.core.repository;

import java.io.File;

import org.netmelody.docnap.core.published.IDocnapStore;
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

}
