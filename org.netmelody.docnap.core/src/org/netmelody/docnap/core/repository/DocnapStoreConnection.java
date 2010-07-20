package org.netmelody.docnap.core.repository;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import org.netmelody.docnap.core.exception.DocnapRuntimeException;
import org.picocontainer.Startable;

public final class DocnapStoreConnection implements IDocnapStoreConnection, Startable {

    private static final String DRIVER = "org.hsqldb.jdbcDriver";
    private static final String DATABASE_CONNECTION_STRING_PREFIX = "jdbc:hsqldb:file:";
    private static final String DATABASE_NAME = "docnap";
    private static final String DATABASE_PRODUCT = "hsql";
    
    private Connection connection;
    private final String userId = "SA";
    private final String password = "";
    
    @Override
    public void start() {
        try {
            Class.forName(DRIVER);
        }
        catch (ClassNotFoundException exception) {
            throw new DocnapRuntimeException("Failed to install HSQLDB JDBC driver.", exception);
        }
    }

    @Override
    public void stop() {
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
    
    public void setStorageLocation(File path) {
        stop();
        
        String databasePath;
        try {
            databasePath = new File(path, DATABASE_NAME).getCanonicalPath();
        }
        catch (IOException exception) {
            throw new DocnapRuntimeException("Failed to calculate database path at [" + path + "].", exception);
        }
        
        try {
            this.connection = DriverManager.getConnection(DATABASE_CONNECTION_STRING_PREFIX + databasePath, this.userId, this.password);
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to open database at [" + path + "].", exception);
        }
    }
    
    public File getStorageLocation() {
        try {
            final String url = this.connection.getMetaData().getURL();
            return new File(url.substring(DATABASE_CONNECTION_STRING_PREFIX.length(), url.length() - DATABASE_NAME.length()));
        }
        catch (Exception exception) {
            throw new DocnapRuntimeException("Failed to get storage location.", exception);
        }
    }

    @Override
    public String getDatabaseProduct() {
        return DATABASE_PRODUCT;
    }

    @Override
    public String getDatabaseUrl() {
        try {
            return this.connection.getMetaData().getURL();
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to get database URL.", exception);
        }
    }

    @Override
    public String getDriverClassName() {
        return DRIVER;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUserId() {
        return this.userId;
    }

    @Override
    public boolean tableExists(String tableName) {
        try {
            return this.connection.getMetaData().getTables(null, null, tableName, null).first();
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to establish existence of table " + tableName, exception);
        }
    }
    
    public PreparedStatement prepareStatement(String expression) {
    	try {
    	  return this.connection.prepareStatement(expression);
    	}
    	catch(SQLException exception) {
            throw new DocnapRuntimeException("Failed to prepare statement: " + expression, exception);
        }
    }
}
