package org.netmelody.docnap.core.repository;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.netmelody.docnap.core.exception.DocnapRuntimeException;

public class DocnapDmlStatement extends DocnapStatement<Integer> {

	public DocnapDmlStatement(IDocnapStoreConnection connection,
			String sqlStatement) {
		super(connection, sqlStatement);
	}
	
	public Integer execute() {
		final PreparedStatement statement = getPreparedStatment();
		
		int result = 0;
        try {
            result = statement.executeUpdate();
            if (-1 == result) {
                throw new DocnapRuntimeException("Failed to execute statement: " + statement.toString());
            }
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to execute statement: " + statement.toString(), exception);
        }
        
        return new Integer(result);
	}

}


