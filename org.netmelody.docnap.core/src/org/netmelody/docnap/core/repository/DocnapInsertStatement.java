package org.netmelody.docnap.core.repository;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.netmelody.docnap.core.exception.DocnapRuntimeException;

public final class DocnapInsertStatement extends DocnapDmlStatement {
	
	private static final String GET_IDENTITY_EXPRESSION = "CALL IDENTITY();";
	private final DocnapSelectStatement getIdentityStatement;

	public DocnapInsertStatement(IDocnapStoreConnection connection, String sqlStatement) {
		super(connection, sqlStatement);
		
		getIdentityStatement = new DocnapSelectStatement(connection, GET_IDENTITY_EXPRESSION);
	}
	
	protected Integer execute() {
		super.execute();
		
		Integer retVal = null;
		
		final ResultSet result = getIdentityStatement.execute();
        try {
            result.next();
            retVal = result.getInt(1);
            result.close();
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to get identity of inserted row.", exception);
        }
        return retVal;
	}

}


