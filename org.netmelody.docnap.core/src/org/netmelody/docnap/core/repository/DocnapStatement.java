package org.netmelody.docnap.core.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.netmelody.docnap.core.exception.DocnapRuntimeException;

public abstract class DocnapStatement<T> {
	
	private final IDocnapStoreConnection connection;
	private final String sqlStatement;
	private PreparedStatement preparedStatement;
	
	public DocnapStatement(IDocnapStoreConnection connection, String sqlStatement) {
		this.connection = connection;
		this.sqlStatement = sqlStatement;
	}
	
	protected PreparedStatement getPreparedStatment() {
		if (null == preparedStatement) {
			preparedStatement = this.connection.prepareStatement(sqlStatement);
		}
		return preparedStatement;
	}
	
	protected abstract T execute();
	
	public T execute(Object[] args) {
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
