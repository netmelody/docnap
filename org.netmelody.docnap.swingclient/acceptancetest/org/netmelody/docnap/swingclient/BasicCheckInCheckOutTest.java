package org.netmelody.docnap.swingclient;


import org.junit.Before;
import org.junit.Test;

public class BasicCheckInCheckOutTest extends DocnapEndToEndTest {

    @Before
    public void runTheApplication() {
        startTheApplication();
    }

    @Test
    public void testCheckingInADocumentAndGettingItOutAgain() {
        final String inFilename = aNewFileCalled("inFile.txt");
        final String outFilename = aNewEmptyFileCalled("outFile.txt");
        final String title = "myTitle";
        
        docnap().indexANewFileCalled(inFilename).andTitleIt(title);
        docnap().viewDetailsOfDocumentTitled(title).andSaveTheDocumentFileTo(outFilename);
    }
    
}
