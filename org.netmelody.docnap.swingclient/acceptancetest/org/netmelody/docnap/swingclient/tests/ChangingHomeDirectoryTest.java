package org.netmelody.docnap.swingclient.tests;

import org.junit.Test;
import org.netmelody.docnap.swingclient.testsupport.DocnapAutoStartingEndToEndTest;

public class ChangingHomeDirectoryTest extends DocnapAutoStartingEndToEndTest{
    
    @Test
    public void changeHomeDirectory() {
        final String inFilename = given().theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String title = given().aDocumentTitle();
        final String tagTitle = given().aTagTitle();
        
        theUserTriesTo().
            indexANewFileCalled(inFilename).
            andTitleIt(title).
            andTagIt(tagTitle).
            andCloseTheWindow();
        
        final String newHome = given().theFullPathToANewHomeDirectory();
        theUserTriesTo().chooseANewHomeFolderOf(newHome);
    
        docnap().hasNoTagTitled(tagTitle);
        docnap().hasNoDocumentTitled(title);
    }
    
    @Test
    public void changeHomeDirectoryAndIndexFileInNewDirectory() {
        final String inFilename = given().theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String title1 = given().aDocumentTitle();
        final String tagTitle1 = given().aTagTitle();
        
        theUserTriesTo()
            .indexANewFileCalled(inFilename)
            .andTitleIt(title1)
            .andTagIt(tagTitle1)
            .andCloseTheWindow();

        
        final String title2 = given().aDocumentTitle();
        final String tagTitle2 = given().aTagTitle();
        final String newHome = given().theFullPathToANewHomeDirectory();
        
        theUserTriesTo().chooseANewHomeFolderOf(newHome);
        theUserTriesTo().indexANewFileCalled(inFilename)
            .andTitleIt(title2)
            .andTagIt(tagTitle2)
            .andCloseTheWindow();
        
        docnap().hasNoTagTitled(tagTitle1);
        docnap().hasNoDocumentTitled(title1);
    }

}
