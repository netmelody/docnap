package org.netmelody.docnap.core.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    private final DocnapSelectStatement checkTagDocumentLinkStatement;
    private final DocnapInsertStatement insertDocumentTagLinkStatement;
    private final DocnapDmlStatement deleteLinkToDocumentStatement;
    private final DocnapSelectStatement fetchTagByIdStatement;
    private final DocnapInsertStatement createTagStatement;
    private final DocnapSelectStatement checkTagExistsStatement;
    private final DocnapSelectStatement fetchTagByTitleStatement;
    private final DocnapSelectStatement findUnlinkedTagsByDocumentIdStatement;
    
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
    
    private static final String CHECK_TAG_DOCUMENT_LINK_EXPRESSION = 
    	"SELECT COUNT(*) FROM documenttaglinks " +
        " WHERE documentid = ? AND tagId = ?";
    
    private static final String INSERT_DOCUMENT_TAG_LINK_EXPRESSION = 
        "INSERT INTO documenttaglinks (documentid, tagid) " +
        "VALUES (?, ?)";
    
    private static final String DELETE_LINK_TO_DOCUMENT_EXPRESSION =
    	"DELETE FROM documenttaglinks WHERE tagid = ? and documentid = ?";
    
    private static final String FETCH_TAG_BY_ID = 
    	"SELECT tagid, creation_dt, title, description," +
        "       (select count(*) from DOCUMENTTAGLINKS tl WHERE tl.tagid = t.tagid) documentCount" +
        "  FROM TAGS t WHERE tagid = ?";
    
    private static final String CREATE_TAG_EXPRESSION = 
    	"INSERT INTO TAGS (title) VALUES (?);";
    
    private static final String CHECK_TAG_EXISTS_EXPRESSION = 
    	"SELECT COUNT(*) FROM tags WHERE upper(title) = upper(?)";
    
    private static final String FETCH_TAG_BY_TITLE_EXPRESSION = 
    	"SELECT tagid, creation_dt, title, description," +
        "       (select count(*) from DOCUMENTTAGLINKS tl WHERE tl.tagid = t.tagid) documentCount" +
        "  FROM TAGS t WHERE upper(title) = upper(?)";
    
    private static final String FIND_UNLINKED_TAGS_BY_DOCUMENT_ID_EXPRESSION = 
    	"SELECT tagid, creation_dt, title, description," +
        "       (select count(*) from DOCUMENTTAGLINKS tl WHERE tl.tagid = t.tagid) documentCount" +
        "  FROM TAGS t " +
        " WHERE NOT EXISTS (SELECT 1 " +
        "                     FROM DOCUMENTTAGLINKS l" +
        "                    WHERE l.documentid = ?" +
        "                      AND l.tagid = t.tagid)";

    public TagRepository(IDocnapStoreConnection connection) {
        this.connection = connection;
        
        fetchAllStatement = new DocnapSelectStatement(this.connection, FETCH_ALL_EXPRESSION);
        findByDocumentIdStatement = new DocnapSelectStatement(this.connection, FIND_BY_DOCUMENT_ID_EXPRESSION);
        checkTagDocumentLinkStatement = new DocnapSelectStatement(this.connection, CHECK_TAG_DOCUMENT_LINK_EXPRESSION);
        insertDocumentTagLinkStatement = new DocnapInsertStatement(this.connection, INSERT_DOCUMENT_TAG_LINK_EXPRESSION);
        deleteLinkToDocumentStatement = new DocnapDmlStatement(this.connection, DELETE_LINK_TO_DOCUMENT_EXPRESSION);
        fetchTagByIdStatement = new DocnapSelectStatement(this.connection, FETCH_TAG_BY_ID);
        createTagStatement = new DocnapInsertStatement(this.connection, CREATE_TAG_EXPRESSION);
        checkTagExistsStatement = new DocnapSelectStatement(this.connection, CHECK_TAG_EXISTS_EXPRESSION);
        fetchTagByTitleStatement = new DocnapSelectStatement(this.connection, FETCH_TAG_BY_TITLE_EXPRESSION);
        findUnlinkedTagsByDocumentIdStatement = new DocnapSelectStatement(this.connection, FIND_UNLINKED_TAGS_BY_DOCUMENT_ID_EXPRESSION);
    }
    
    public List<Tag> fetchAll() {
        return fetchMultipleWithSql(fetchAllStatement, null);
    }

    public Collection<Tag> findByDocumentId(Integer identity) {
        return fetchMultipleWithSql(findByDocumentIdStatement, new Object[] {identity});
    }
    
    public Collection<Tag> findUnlinkedByDocumentId(Integer identity) {
        return fetchMultipleWithSql(findUnlinkedTagsByDocumentIdStatement, new Object[] {identity});
    }

    public Tag tagDocumentById(Integer documentId, String tagTitle) {
        final Tag tag = createTag(tagTitle);
        final Integer tagId = tag.getIdentity();
        
        final ResultSet resultSet = checkTagDocumentLinkStatement.execute(new Object[] {documentId, tagId});
        try {
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                return tag;
            }
        }
        catch (SQLException exception) {
            throw new DocnapRuntimeException("Failed to find tag link.", exception);
        }
        
        insertDocumentTagLinkStatement.execute(new Object[] {documentId, tagId});
        return tag;
    }
    
    public void unTagDocumentById(Integer documentId, String tagTitle) {
        if (!tagExists(tagTitle)) {
            return;
        }
        
        deleteLinkToDocumentStatement.execute(new Object[] {fetchByTitle(tagTitle).getIdentity(), documentId});
    }

    public Tag fetchById(Integer identity) {
        return fetchSingleWithSql(fetchTagByIdStatement, new Object[] {identity});
    }
    
    private Tag createTag(String tagTitle) {
        if (tagExists(tagTitle)) {
            return fetchByTitle(tagTitle);
        }

        final Integer identity = createTagStatement.execute(new Object[] {tagTitle});

        return fetchById(identity);
    }

    private boolean tagExists(final String tagTitle) {
        final ResultSet resultSet = checkTagExistsStatement.execute(new Object[] {tagTitle});
        try {
            resultSet.next();
            return 0 < resultSet.getInt(1);
        }
        catch (SQLException e) {
            return false;
        }
    }

    private Tag fetchByTitle(String tagTitle) {
        return fetchSingleWithSql(fetchTagByTitleStatement, new Object[] {tagTitle});
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
    
    private Tag fetchSingleWithSql(DocnapSelectStatement statement, Object[] args) {
        final ResultSet resultSet = statement.execute(args);
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
