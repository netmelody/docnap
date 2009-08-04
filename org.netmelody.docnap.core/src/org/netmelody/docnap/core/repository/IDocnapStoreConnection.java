package org.netmelody.docnap.core.repository;

import java.io.File;
import java.sql.ResultSet;

public interface IDocnapStoreConnection {

	void setStorageLocation(File path);
	
	File getStorageLocation();

	String getUserId();
	
	String getPassword();
	
	String getDriverClassName();
	
	String getDatabaseUrl();
	
	String getDatabaseProduct();
	
	boolean tableExists(String tableName);

	/**
	 * @deprecated use executeDml instead
	 */
	@Deprecated
	int executeStatement(String expression);

	void executeDml(String expression);
	
	Integer executeInsert(String expression);
	
	ResultSet executeSelect(String expression);
	
}
