package org.netmelody.docnap.swingclient.tests;

import org.junit.Test;
import org.netmelody.docnap.swingclient.testsupport.DocnapAutoStartingEndToEndTest;
import org.netmelody.docnap.swingclient.testsupport.driver.DocnapDocumentDetailsFrameDriver;

public class BasicAddTagTest extends DocnapAutoStartingEndToEndTest {

    @Test
    public void testCheckingInADocumentAndAddingTag() {
        final String inFilename = given().theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String title = given().aDocumentTitle();
        final String tagTitle = given().aTagTitle();
        
        theUserTriesTo()
            .indexANewFileCalled(inFilename)
            .andTitleIt(title)
            .andTagIt(tagTitle)
            .andCloseTheWindow();
        
        theUserTriesTo().selectTheTagCalled(tagTitle);
        theUserTriesTo().andViewDetailsofDocumentTitled(title);
        
        final DocnapDocumentDetailsFrameDriver detailWindow =
            docnap().showsDocumentDetailsForADocumentTitled(title);
        detailWindow.showsTag(tagTitle);
        detailWindow.dispose(); //TODO: Dispose should not be part of the test, cleanup should be elsewhere.
    }
    
    @Test
    public void testCheckingInADocumentSelectingAndAddingTag() {
        final String inFilename = given().theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String title = given().aDocumentTitle();
        final String tagTitle = given().aTagTitle();
        
        theUserTriesTo().indexANewFileCalled(inFilename).andTitleIt(title).andCloseTheWindow();
        
        theUserTriesTo()
            .selectTheAllTagAndViewDetailsOfTheDocumentTitled(title)
            .andTagIt(tagTitle)
            .andCloseTheWindow();
        
        theUserTriesTo().selectTheTagCalled(tagTitle);
        theUserTriesTo().andViewDetailsofDocumentTitled(title);
    
        final DocnapDocumentDetailsFrameDriver detailWindow =
            docnap().showsDocumentDetailsForADocumentTitled(title);
        detailWindow.showsTag(tagTitle);
        detailWindow.dispose(); //TODO: Dispose should not be part of the test, cleanup should be elsewhere.
    }
    

}
