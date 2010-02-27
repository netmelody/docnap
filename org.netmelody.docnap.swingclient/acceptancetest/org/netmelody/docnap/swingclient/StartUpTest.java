package org.netmelody.docnap.swingclient;

import org.junit.Test;

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
        docnap().startWithNewSettingsStoredAt(theFullPathToANewFolderCalled("mySettings"));

        final String myHomeFolderPath = theFullPathToANewFolderCalled("myHomeDirectory");
        docnap().chooseHomeFolderOf(myHomeFolderPath);
        docnap().showsMainFrameWithTitleContaining(myHomeFolderPath);
    }
    
    /**
     * Test starting the application, and cancelling the selection of a
     * home directory.
     * 
     * Expect that the application exits without error.
     */
    @Test
    public void testStartingAndCancellingTheSelectionOfAHomeDirectory() {
        docnap().startWithNewSettingsStoredAt(theFullPathToANewFolderCalled("mySettings"));

        docnap().cancelHomeFolderSelection();
        docnap().hasClosed();
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
        docnap().startWithNewSettingsStoredAt(settingsDirectoryPath);
        final String myHomeFolderPath = theFullPathToANewFolderCalled("myHomeDirectory");
        docnap().chooseHomeFolderOf(myHomeFolderPath);
        docnap().showsMainFrameWithTitleContaining(myHomeFolderPath);
        
        //TODO: This exits the app, which the app-framework enacts with a System.exit - bummer
        docnap().exitTheApplication();
        
        // Re-launch the application with existing settings
        docnap().startWithExistingSettingsStoredAt(settingsDirectoryPath);
        docnap().showsMainFrameWithTitleContaining(myHomeFolderPath);
    }
}
