package org.netmelody.docnap.swingclient.tests;

import org.junit.Test;
import org.netmelody.docnap.swingclient.testsupport.DocnapAutoStartingEndToEndTest;
import org.netmelody.docnap.swingclient.testsupport.driver.DocnapDocumentDetailsFrameDriver;

public class BasicAddTagTest extends DocnapAutoStartingEndToEndTest {

    @Test
    public void testCheckingInADocumentAndAddingTag() {
        final String inFilename = given().theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String title = "myTitle";
        final String tagTitle = "FirstTag";
        
        theUserTriesTo().
            indexANewFileCalled(inFilename).
            andTitleIt(title).
            andTagIt(tagTitle).
            andCloseTheWindow();
        
        theUserTriesTo().selectTheTagCalled(tagTitle).andViewDetailsofDocumentTitled(title);
        
        final DocnapDocumentDetailsFrameDriver detailWindow =
            docnap().showsDocumentDetailsForADocumentTitled(title);
        detailWindow.showsTag(tagTitle);
        detailWindow.dispose();
    }
    
    @Test
    public void testCheckingInADocumentSelectingAndAddingTag() {
        final String inFilename = given().theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String title = "myTitle";
        final String tagTitle = "FirstTag";
        
        theUserTriesTo().indexANewFileCalled(inFilename).andTitleIt(title).andCloseTheWindow();
        theUserTriesTo().viewDetailsOfTheDocumentTitled(title).andTagIt(tagTitle).andCloseTheWindow();
        theUserTriesTo().selectTheTagCalled(tagTitle).andViewDetailsofDocumentTitled(title);
    
        final DocnapDocumentDetailsFrameDriver detailWindow =
            docnap().showsDocumentDetailsForADocumentTitled(title);
        detailWindow.showsTag(tagTitle);
        detailWindow.dispose();
    }
    

}
