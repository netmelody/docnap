package org.netmelody.docnap.core.published.tests;

import static org.hamcrest.CoreMatchers.equalTo;

import java.io.File;

import org.junit.Test;
import org.netmelody.docnap.core.testsupport.DocnapCoreAcceptanceTest;

public class BasicDocumentHandlingTest extends DocnapCoreAcceptanceTest {
    
    @Test public void
    supportsAddingADocument() {
        File file = given().aNewPopulatedFile();
        when().aRequestIsMadeTo().addADocumentForFile(file);
        then().theStore().hasOneDocumentContaining(file);
    }
    
    @Test public void
    supportsAddingADocumentAndTaggingIt() {
        File file = given().aNewPopulatedFile();
        String tagTitle = given().aTagTitle();
        
        when().aRequestIsMadeTo().addADocumentForFile(file)
        .and().aRequestIsMadeTo().tagTheLastDocumentAddedWithATagTitled(tagTitle);
        
        then().theStore().hasOneDocumentContaining(file).tagged(tagTitle);
    }
    
    @Test public void
    supportsAddingTwoDocumentsAndTaggingOneOfThem() {
        String tagTitle = given().aTagTitle();
        
        when().aRequestIsMadeTo().addADocument()
        .and().aRequestIsMadeTo().addADocument()
        .and().aRequestIsMadeTo().tagTheLastDocumentAddedWithATagTitled(tagTitle);
        
        then().theStore().hasANumberOfDocumentsTagged(tagTitle, equalTo(1))
        .and().theStore().hasANumberOfDocumentsWithNoTag(equalTo(1));
    }
    
    @Test public void
    supportsTaggingADocumentWithTwoTags() {
        String title1 = given().aTagTitle();
        String title2 = given().aTagTitle();
        
        givenAStore().containingOneDocument();
        
        when().aRequestIsMadeTo().tagTheLastDocumentAddedWithATagTitled(title1);
        when().aRequestIsMadeTo().tagTheLastDocumentAddedWithATagTitled(title2);
        
        then().theStore().hasANumberOfDocumentsTagged(title1, equalTo(1))
        .and().theStore().hasANumberOfDocumentsTagged(title2, equalTo(1));
    }
    
    @Test public void
    supportsRemovingADocument() {
        givenAStore().containingOneDocument();
        
        when().aRequestIsMadeTo().removeTheLastDocumentAdded();
        
        then().theStore().isEmpty();
    }
}
