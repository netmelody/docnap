package org.netmelody.docnap.core.repository;

import java.io.File;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public interface IDocnapStoreConnection {

    void setStorageLocation(File path);
    
    File getStorageLocation();

    String getUserId();
    
    String getPassword();
    
    String getDriverClassName();
    
    String getDatabaseUrl();
    
    String getDatabaseProduct();
    
    boolean tableExists(String tableName);

    void executeDml(String expression);
    
    Integer executeInsert(String expression);
    
    ResultSet executeSelect(String expression);
    
    ResultSet executeSelect(PreparedStatement statement);
    
    PreparedStatement prepareStatement(String expression);
    
}
