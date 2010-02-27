package org.netmelody.docnap.swingclient.tests;

import org.junit.Test;
import org.netmelody.docnap.swingclient.testsupport.DocnapEndToEndTest;

/**
 * Tests concerned with starting up the application.
 * 
 * @author Tom Denley
 *
 */
public class StartUpTest extends DocnapEndToEndTest {

    /**
     * Test that the application can be started, and a home directory chosen
     * by the user.
     * 
     * Expect that the main frame is shown and that the title
     * bar contains the chosen path.
     */
    @Test
    public void testStartingAndSelectingAHomeDirectory() {
        theUserTriesTo().startWithNewSettingsStoredAt(theFullPathToANewFolderCalled("mySettings"));

        final String myHomeFolderPath = theFullPathToANewFolderCalled("myHomeDirectory");
        theUserTriesTo().chooseHomeFolderOf(myHomeFolderPath);
        theUserTriesTo().showsMainFrameWithTitleContaining(myHomeFolderPath);
    }
    
    /**
     * Test starting the application, and cancelling the selection of a
     * home directory.
     * 
     * Expect that the application exits without error.
     */
    @Test
    public void testStartingAndCancellingTheSelectionOfAHomeDirectory() {
        theUserTriesTo().startWithNewSettingsStoredAt(theFullPathToANewFolderCalled("mySettings"));

        theUserTriesTo().cancelHomeFolderSelection();
        theUserTriesTo().hasClosed();
    }
    
    /**
     * Test starting the application a second time, pointing at the same
     * settings directory, and hence picking up the same home directory as
     * before.
     * 
     * Expect that the application launches immediately to the main screen
     * and is showing the correct home directory in the title bar.
     */
    @Test
    public void testStartingWithHomeDirectoryRemembered() {
        // Launch for the first time and establish home directory
        final String settingsDirectoryPath = theFullPathToANewFolderCalled("mySettings");
        theUserTriesTo().startWithNewSettingsStoredAt(settingsDirectoryPath);
        final String myHomeFolderPath = theFullPathToANewFolderCalled("myHomeDirectory");
        theUserTriesTo().chooseHomeFolderOf(myHomeFolderPath);
        theUserTriesTo().showsMainFrameWithTitleContaining(myHomeFolderPath);
        
        //TODO: This exits the app, which the app-framework enacts with a System.exit - bummer
        theUserTriesTo().exitTheApplication();
        
        // Re-launch the application with existing settings
        theUserTriesTo().startWithExistingSettingsStoredAt(settingsDirectoryPath);
        theUserTriesTo().showsMainFrameWithTitleContaining(myHomeFolderPath);
    }
}
