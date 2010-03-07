package org.netmelody.docnap.swingclient.tests;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import static org.junit.Assert.*;
import org.junit.Test;
import org.netmelody.docnap.swingclient.testsupport.DocnapAutoStartingEndToEndTest;

/**
 * Basic test checking in and out a file.
 * 
 * @author Tom Denley
 *
 */
public class BasicCheckInCheckOutTest extends DocnapAutoStartingEndToEndTest {

    /**
     * Test checking in and out a document.
     */
    @Test
    public void
    supportsCheckingInADocumentAndGettingItOutAgain() throws IOException {
        final String inFilename = given().theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String outFilename = given().theFullPathToNewNonExistantFileCalled("outFile.txt");
        final String title = "theTitleOfMyDocument";
        
        theUserTriesTo().indexANewFileCalled(inFilename)
            .andTitleIt(title)
            .andCloseTheWindow();
        
        theUserTriesTo().selectTheAllTagAndViewDetailsOfTheDocumentTitled(title)
            .andSaveTheDocumentFileTo(outFilename)
            .andCloseTheWindow();
        
        assertTrue("Retrieved file has incorrect content.",
                   FileUtils.contentEquals(new File(inFilename), new File(outFilename)));
    }
    
}
