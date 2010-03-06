package org.netmelody.docnap.swingclient.tests;

import org.junit.Test;
import org.netmelody.docnap.swingclient.testsupport.DocnapAutoStartingEndToEndTest;

public class ChangingHomeDirectoryTest extends DocnapAutoStartingEndToEndTest{
    
    @Test
    public void changeHomeDirectory() {
        final String inFilename = theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String title = "myTitle";
        final String tagTitle = "FirstTag";
        
        theUserTriesTo().
            indexANewFileCalled(inFilename).
            andTitleIt(title).
            andTagIt(tagTitle).
            andCloseTheWindow();
        
        theUserTriesTo().selectSelectHomeDirectory().chooseAHomeFolderOf(theFullPathToANewFolderCalled("SecondHomeDirectory"));
    
        docnap().hasNoTagTitled(tagTitle);
        docnap().hasNoDocumentTitled(title);
    }
    
    @Test
    public void changeHomeDirectoryAndIndexFileInNewDirectory() {
        final String inFilename = theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String title = "myTitle";
        final String tagTitle = "FirstTag";
        final String secondTitle = "SecondDirectory";
        final String secondTagTitle = "SecondTag";
        
        theUserTriesTo().
            indexANewFileCalled(inFilename).
            andTitleIt(title).
            andTagIt(tagTitle).
            andCloseTheWindow();
        
        theUserTriesTo().selectSelectHomeDirectory().chooseAHomeFolderOf(theFullPathToANewFolderCalled("SecondHomeDirectory"));
        theUserTriesTo().indexANewFileCalled(inFilename).
            andTitleIt(secondTitle).
            andTagIt(secondTagTitle).
            andCloseTheWindow();
        
        docnap().hasNoTagTitled(tagTitle);
        docnap().hasNoDocumentTitled(title);
    }

}
