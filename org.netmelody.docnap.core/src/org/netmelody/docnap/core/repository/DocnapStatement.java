package org.netmelody.docnap.core.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.netmelody.docnap.core.exception.DocnapRuntimeException;

abstract class DocnapStatement<T> {
	
	private final IDocnapStoreConnection connection;
	private final String sqlStatement;
	private PreparedStatement preparedStatement;
	
	/**
	 * DocnapStatement
	 * 
	 * @param connection
	 *     This is the IDocnapStoreConnection that will be used to run the 
	 *     SQL statement
	 * @param sqlStatement
	 *     This the SQL statement that will be run when the execute command
	 *     is called
	 */
	public DocnapStatement(IDocnapStoreConnection connection, String sqlStatement) {
		this.connection = connection;
		this.sqlStatement = sqlStatement;
	}
	
	protected final PreparedStatement getPreparedStatment() {
		if (null == preparedStatement) {
			preparedStatement = this.connection.prepareStatement(sqlStatement);
		}
		return preparedStatement;
	}
	
	protected abstract T execute();
	
	//TODO change to varargs?
	public final T execute(Object[] args) {
		final PreparedStatement statement = getPreparedStatment();
		
		if (args != null) {
		  for (int arrayIndex = 0; arrayIndex < args.length; arrayIndex++) {	
			final int parameterIndex = arrayIndex + 1;
		    try {
		      statement.setObject(parameterIndex , args[arrayIndex]);
		    }
		    catch (SQLException exception) {
		      throw new DocnapRuntimeException("Failed to set parameter " + parameterIndex + " for : " + sqlStatement, exception);
		    }
		  }
		}
		
		return execute();
	}

}
