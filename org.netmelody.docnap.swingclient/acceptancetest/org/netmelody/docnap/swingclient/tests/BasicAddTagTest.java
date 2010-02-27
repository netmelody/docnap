package org.netmelody.docnap.swingclient.tests;

import org.junit.Ignore;
import org.junit.Test;
import org.netmelody.docnap.swingclient.testsupport.DocnapAutoStartingEndToEndTest;
import org.netmelody.docnap.swingclient.testsupport.driver.DocnapDocumentDetailsFrameDriver;

public class BasicAddTagTest extends DocnapAutoStartingEndToEndTest {

    

    
    @Test
    public void testCheckingInADocumentAndAddingTag() {
        final String inFilename = theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String title = "myTitle";
        final String tagTitle = "FirstTag";
        
        theUserTriesTo().
            indexANewFileCalled(inFilename).
            andTitleIt(title).
            andTagIt(tagTitle).
            andCloseTheWindow();
        
        theUserTriesTo().viewDetailsofDocumentTitledWithTag(title, tagTitle);
        
        final DocnapDocumentDetailsFrameDriver detailWindow =
            docnap().showsDocumentDetailsForADocumentTitled(title);
        detailWindow.showsTag(tagTitle);
    }
    
    @Ignore
    @Test
    public void testCheckingInADocumentSelectingAndAddingTag() {
        final String inFilename = theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String title = "myTitle";
        final String tagTitle = "FirstTag";
        
        theUserTriesTo().indexANewFileCalled(inFilename).andTitleIt(title).andCloseTheWindow();
        theUserTriesTo().viewDetailsOfDocumentTitled(title).andTagIt(tagTitle).andCloseTheWindow();
        theUserTriesTo().viewDetailsofDocumentTitledWithTag(title, tagTitle); //.andCheckTheTags(new String[] {tagTitle});
    }
    

}
