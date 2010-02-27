package org.netmelody.docnap.swingclient;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Basic test checking in and out a file.
 * 
 * @author Tom Denley
 *
 */
public class BasicCheckInCheckOutTest extends DocnapEndToEndTest {

    @Before
    public void runTheApplication() {
        startTheApplication();
    }

    /**
     * Test checking in and out a document.
     */
    @Test
    public void
    supportsCheckingInADocumentAndGettingItOutAgain() throws IOException {
        final String inFilename = theFullPathToANewPopulatedFileCalled("inFile.txt");
        final String outFilename = theFullPathToNewNonExistantFileCalled("outFile.txt");
        final String title = "theTitleOfMyDocument";
        
        docnap().indexANewFileCalled(inFilename).andTitleIt(title);
        docnap().viewDetailsOfDocumentTitled(title).andSaveTheDocumentFileTo(outFilename);
        
        assertTrue("Retrieved file has incorrect content.", FileUtils.contentEquals(new File(inFilename), new File(outFilename)));
    }
    
}
