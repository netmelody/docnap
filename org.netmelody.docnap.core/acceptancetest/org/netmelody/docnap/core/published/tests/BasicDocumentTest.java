package org.netmelody.docnap.core.published.tests;

import java.io.File;

import org.junit.Test;
import org.netmelody.docnap.core.testsupport.DocnapCoreAcceptanceTest;

public class BasicDocumentTest extends DocnapCoreAcceptanceTest {
    
    @Test
    public void supportsAddingADocument() {
        File file = given().aNewPopulatedFile();
        when().aRequestIsMadeTo().addADocumentForFile(file);
        then().theStore().hasOneDocumentContaining(file);
    }
    
    @Test
    public void supportsAddingADocumentAndTaggingIt() {
        File file = given().aNewPopulatedFile();
        String tagTitle = given().aTagTitle();
        
        when().aRequestIsMadeTo().addADocumentForFile(file)
        .and().aRequestIsMadeTo().tagTheLastDocumentAddedWithTagTitled(tagTitle);
        
        then().theStore().hasOneDocumentContaining(file).tagged(tagTitle);
    }
}
