package org.netmelody.docnap.core.published.tests;

import java.io.File;

import org.junit.Test;
import org.netmelody.docnap.core.testsupport.DocnapCoreAcceptanceTest;

public final class BasicDocumentHandlingTest extends DocnapCoreAcceptanceTest {
    
    @Test public void
    supportsAddingADocument() {
        File file = given().aNewPopulatedFile();
        when().aRequestIsMadeTo().addADocumentForFile(file);
        then().theStore().hasOneDocumentContaining(file);
    }
    
    @Test public void
    supportsRemovingADocument() {
        givenAStore().containingOneDocument();
        when().aRequestIsMadeTo().removeTheLastDocumentAdded();
        then().theStore().isEmpty();
    }
    
    @Test public void
    supportsGivingADocumentATitle() {
        String title = given().aDocumentTitle();
        givenAStore().containingOneDocument();
        when().aRequestIsMadeTo().titleTheLastDocumentAdded(title);
        then().theStore().hasOneDocument().titled(title);
    }
}
