package org.netmelody.docnap.swingclient.tests;

import org.junit.Test;
import org.netmelody.docnap.swingclient.testsupport.DocnapEndToEndTest;
import org.netmelody.docnap.swingclient.testsupport.ExitHandling;

/**
 * Tests concerned with starting up the application.
 * 
 * @author Tom Denley
 *
 */
public class ApplicationLaunchTest extends DocnapEndToEndTest {

    /**
     * Test that the application can be started, and a home directory chosen
     * by the user.
     * 
     * Expect that the main frame is shown and that the title
     * bar contains the chosen path.
     */
    @Test
    public void
    supportsStartingDocnapAndSelectingAHomeDirectory() {
        final String settings = given().theFullPathToANewSettingsDirectory();
        theUserTriesTo().startDocnapWithNewSettingsStoredAt(settings);

        final String myHomeFolderPath = given().theFullPathToANewHomeDirectory();
        theUserTriesTo().chooseAHomeFolderOf(myHomeFolderPath);
        docnap().showsMainFrameWithTitleContaining(myHomeFolderPath);
    }
    
    /**
     * Test starting the application, and cancelling the selection of a
     * home directory.
     * 
     * Expect that the application exits without error.
     */
    @Test
    public void
    supportsStartingDocnapAndCancellingTheSelectionOfAHomeDirectory() {
        final String settings = given().theFullPathToANewSettingsDirectory();
        theUserTriesTo().startDocnapWithNewSettingsStoredAt(settings);

        theUserTriesTo().cancelHomeFolderSelection();
        
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
    public void
    supportsStartingWithHomeDirectoryRememberedFromLastTime() {
        // TODO Tom - move to setup and tear down? Can potentially also solve with JMockKit
        ExitHandling.overrideSecurityManager();
        
        final String settingsDirectoryPath = given().theFullPathToANewSettingsDirectory();
        final String myHomeFolderPath = given().theFullPathToANewHomeDirectory();
        
        // Launch for the first time and establish home directory
        theUserTriesTo().startDocnapWithNewSettingsStoredAt(settingsDirectoryPath);
        theUserTriesTo().chooseAHomeFolderOf(myHomeFolderPath);
        docnap().showsMainFrameWithTitleContaining(myHomeFolderPath);
        
        //TODO: Find a way to catch the exception thrown by the Security Manager
        theUserTriesTo().exitTheApplication();
        
        // Re-launch the application with existing settings
        theUserTriesTo().startDocnapWithExistingSettingsStoredAt(settingsDirectoryPath);
        docnap().showsMainFrameWithTitleContaining(myHomeFolderPath);
        
        ExitHandling.restoreDefaultSecurityManager();
    }
}
