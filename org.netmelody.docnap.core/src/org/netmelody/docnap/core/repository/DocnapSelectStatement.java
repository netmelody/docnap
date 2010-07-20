package org.netmelody.docnap.core.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.netmelody.docnap.core.exception.DocnapRuntimeException;

public class DocnapSelectStatement extends DocnapStatement<ResultSet> {

	public DocnapSelectStatement(IDocnapStoreConnection connection, String sqlStatement) {
		super(connection, sqlStatement);
	}
	
	public ResultSet execute() {
		final PreparedStatement statement = getPreparedStatment();
		
		try {
	      return statement.executeQuery();
	    }
	    catch (SQLException exception) {
	      throw new DocnapRuntimeException("Failed to execute statement: " + statement.toString(), exception);
	    }
	}

}
