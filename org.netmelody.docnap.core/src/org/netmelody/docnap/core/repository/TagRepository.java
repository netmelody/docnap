package org.netmelody.docnap.core.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.exception.DocnapRuntimeException;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.type.DocnapDateTime;

public class TagRepository implements ITagRepository {

    private final IDocnapStoreConnection connection;
    
    private final DocnapSelectStatement fetchAllStatement;
    private final DocnapSelectStatement findByDocumentIdStatement;
    
    private static final String FETCH_ALL_EXPRESSION =  
    	"SELECT tagid, creation_dt, title, description, count(l.documenttaglinkid) documentCount" +
        "  FROM TAGS t LEFT OUTER JOIN DOCUMENTTAGLINKS l" +
        "    ON (t.tagid = l.tagid)" +
        " GROUP BY tagid, creation_dt, title, description" +
        " ORDER BY title asc, count(l.documenttaglinkid) desc";
    
    private static final String FIND_BY_DOCUMENT_ID_EXPRESSION = 
    	"SELECT tagid, creation_dt, title, description," +
        "       (select count(*) from DOCUMENTTAGLINKS tl WHERE tl.tagid = t.tagid) documentCount" +
        "  FROM TAGS t INNER JOIN DOCUMENTTAGLINKS l" +
        "    ON (t.tagid = l.tagid)" +
        " WHERE l.documentid = ?";

    public TagRepository(IDocnapStoreConnection connection) {
        this.connection = connection;
        
        fetchAllStatement = new DocnapSelectStatement(connection, FETCH_ALL_EXPRESSION);
        findByDocumentIdStatement = new DocnapSelectStatement(connection, FIND_BY_DOCUMENT_ID_EXPRESSION);
    }
    
    public List<Tag> fetchAll() {
        return fetchMultipleWithSql(fetchAllStatement, null);
    }

    public Collection<Tag> findByDocumentId(Integer identity) {
        return fetchMultipleWithSql(findByDocumentIdStatement, new Object[] {identity});
    }

    public Tag tagDocumentById(Integer documentId, String tagTitle) {
        final Tag tag = createTag(tagTitle);
        final Integer tagId = tag.getIdentity();
        
        final String countStmt = "SELECT COUNT(*) FROM documenttaglinks " +
                                 " WHERE documentid = " + documentId + " AND tagId = " + tagId;
        final ResultSet resultSet = this.connection.executeSelect(countStmt);
        try {
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                return tag;
            }
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to find tag link.", exception);
        }
        
        final String sqlStmt = "INSERT INTO documenttaglinks (documentid, tagid) " +
                               "VALUES (" + documentId + ", " + tagId + ")";
        this.connection.executeDml(sqlStmt);
        return tag;
    }
    
    public void unTagDocumentById(Integer documentId, String tagTitle) {
        if (!tagExists(tagTitle)) {
            return;
        }
        
        final String sqlStmt = "DELETE FROM documenttaglinks WHERE tagid = " + fetchByTitle(tagTitle).getIdentity();
        this.connection.executeDml(sqlStmt);
    }

    public Tag fetchById(Integer identity) {
        final String sqlStmt = "SELECT tagid, creation_dt, title, description," +
        "       (select count(*) from DOCUMENTTAGLINKS tl WHERE tl.tagid = t.tagid) documentCount" +
        "  FROM TAGS t WHERE tagid = " + identity;
        return fetchSingleWithSql(sqlStmt);
    }
    
    private Tag createTag(String tagTitle) {
        if (tagExists(tagTitle)) {
            return fetchByTitle(tagTitle);
        }

        final String sqlText = "INSERT INTO TAGS (title) VALUES ('" + tagTitle + "');";
        final Integer identity = this.connection.executeInsert(sqlText);

        return fetchById(identity);
    }

    private boolean tagExists(final String tagTitle) {
        final String sqlStmt = "SELECT COUNT(*) FROM tags WHERE upper(title) = upper('" + tagTitle + "')";
        final ResultSet resultSet = this.connection.executeSelect(sqlStmt);
        try {
            resultSet.next();
            return 0 < resultSet.getInt(1);
        }
        catch (SQLException e) {
            return false;
        }
    }

    private Tag fetchByTitle(String tagTitle) {
        final String sqlStmt = "SELECT tagid, creation_dt, title, description," +
        "       (select count(*) from DOCUMENTTAGLINKS tl WHERE tl.tagid = t.tagid) documentCount" +
        "  FROM TAGS t WHERE upper(title) = upper('" + tagTitle + "')";
        return fetchSingleWithSql(sqlStmt);
    }

    @Deprecated
    private List<Tag> fetchMultipleWithSql(String sqlStmt) {
        final ResultSet resultSet = this.connection.executeSelect(sqlStmt);
        final List<Tag> result = new ArrayList<Tag>();
        try {
            while(resultSet.next()) {
                result.add(extractTag(resultSet));
            }
            resultSet.close();
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to retrieve tags", exception);
        }
        return result;
    }
    
    private List<Tag> fetchMultipleWithSql(DocnapSelectStatement statement, Object[] args) {
        final ResultSet resultSet = statement.execute(args);
        final List<Tag> result = new ArrayList<Tag>();
        try {
            while(resultSet.next()) {
                result.add(extractTag(resultSet));
            }
            resultSet.close();
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to retrieve tags", exception);
        }
        return result;
    }
    
    private Tag fetchSingleWithSql(String sqlStmt) {
        final ResultSet resultSet = this.connection.executeSelect(sqlStmt);
        try {
            if (!resultSet.next()) {
                throw new IllegalArgumentException("Invalid Tag identifier");
            }
            return extractTag(resultSet);
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to retrieve tag", exception);
        }
    }
    
    private Tag extractTag(ResultSet resultSet) throws SQLException {
        final Tag tag = new Tag(resultSet.getInt("tagid"));
        tag.setTitle(resultSet.getString("title"));
        tag.setDescription(resultSet.getString("description"));
        tag.setDateCreated(new DocnapDateTime(resultSet.getTimestamp("creation_dt")));
        tag.setDocumentCount(resultSet.getInt("documentCount"));
        return tag;
    }
    
}
