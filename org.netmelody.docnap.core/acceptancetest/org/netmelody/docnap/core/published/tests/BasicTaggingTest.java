package org.netmelody.docnap.core.published.tests;

import static org.hamcrest.CoreMatchers.equalTo;

import java.io.File;

import org.junit.Test;
import org.netmelody.docnap.core.testsupport.DocnapCoreAcceptanceTest;

public class BasicTaggingTest extends DocnapCoreAcceptanceTest {
    
    @Test public void
    supportsTaggingADocument() {
        File file = given().aNewPopulatedFile();
        String tagTitle = given().aTagTitle();
        
        when().aRequestIsMadeTo().addADocumentForFile(file)
        .and().aRequestIsMadeTo().tagTheLastDocumentAddedWithATagTitled(tagTitle);
        
        then().theStore().hasOneDocumentContaining(file).tagged(tagTitle);
    }
    
    @Test public void
    supportsTaggingASingleDocumentAmongstMany() {
        String tagTitle = given().aTagTitle();
        
        when().aRequestIsMadeTo().addADocument()
        .and().aRequestIsMadeTo().addADocument()
        .and().aRequestIsMadeTo().tagTheLastDocumentAddedWithATagTitled(tagTitle);
        
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
}
