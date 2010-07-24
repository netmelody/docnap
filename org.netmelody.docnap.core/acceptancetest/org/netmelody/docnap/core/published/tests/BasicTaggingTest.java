package org.netmelody.docnap.core.published.tests;

import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Test;
import org.netmelody.docnap.core.testsupport.DocnapCoreAcceptanceTest;

public class BasicTaggingTest extends DocnapCoreAcceptanceTest {
    
    @Test public void
    supportsTaggingADocument() {
        String tagTitle = given().aTagTitle();
        givenAStore().containingOneDocument();
        
        when().aRequestIsMadeTo().tagTheLastDocumentAddedWithATagTitled(tagTitle);
        
        then().theStore().hasOneDocument().tagged(tagTitle);
    }
    
    @Test public void
    supportsTaggingASingleDocumentAmongstMany() {
        String tagTitle = given().aTagTitle();
        givenAStore().containingANumberOfDocuments(equalTo(2));
        
        when().aRequestIsMadeTo().tagTheLastDocumentAddedWithATagTitled(tagTitle);
        
        then().theStore().hasANumberOfDocumentsTagged(tagTitle, equalTo(1))
        .and().theStore().hasANumberOfDocumentsWithNoTag(equalTo(1));
    }
    
    @Test public void
    supportsTaggingOneDocumentWithTwoTags() {
        String title1 = given().aTagTitle();
        String title2 = given().aTagTitle();
        
        givenAStore().containingOneDocument();
        
        when().aRequestIsMadeTo().tagTheLastDocumentAddedWithATagTitled(title1);
        when().aRequestIsMadeTo().tagTheLastDocumentAddedWithATagTitled(title2);
        
        then().theStore().hasANumberOfDocumentsTagged(title1, equalTo(1))
        .and().theStore().hasANumberOfDocumentsTagged(title2, equalTo(1));
    }
    
    @Test public void
    supportsRemovingATag() {
        String title = given().aTagTitle();
        givenAStore().containingOneDocument().tagged(title);
        
        when().aRequestIsMadeTo().removeTheTagTitled(title);
        
        then().theStore().hasANumberOfDocumentsWithNoTag(equalTo(1))
        .and().theStore().hasANumberOfTags(equalTo(0));
    }
}
