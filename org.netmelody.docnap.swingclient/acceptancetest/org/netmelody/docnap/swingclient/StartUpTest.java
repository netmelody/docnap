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
        docnap().startWithNewSettingsStoredAt(aNewFolderCalled("mySettings"));

        final String myHomeFolderPath = aNewFolderCalled("myHomeDirectory");
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
        docnap().startWithNewSettingsStoredAt(aNewFolderCalled("mySettings"));

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
        final String settingsDirectoryPath = aNewFolderCalled("mySettings");
        docnap().startWithNewSettingsStoredAt(settingsDirectoryPath);
        final String myHomeFolderPath = aNewFolderCalled("myHomeDirectory");
        docnap().chooseHomeFolderOf(myHomeFolderPath);
        docnap().showsMainFrameWithTitleContaining(myHomeFolderPath);
        docnap().exitTheApplication();
        
        // Re-launch the application with existing settings
        docnap().startWithExistingSettingsStoredAt(settingsDirectoryPath);
        docnap().showsMainFrameWithTitleContaining(myHomeFolderPath);
    }
}
