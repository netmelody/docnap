package org.netmelody.docnap.swingclient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.fail;
import org.junit.rules.TemporaryFolder;

/**
 * Tests concerned with starting up the application.
 * 
 * @author Tom Denley
 *
 */
public class StartUpTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final DocnapApplicationRunner application = new DocnapApplicationRunner();
    
    @Before
    public void checkTemporaryFolderExists() throws IOException {
        assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
    }

    @After
    public void stopTheApplication() {
        application.stop();
    }

    private String aNewFolderCalled(final String name) {
        final File settingsFolder = this.folder.newFolder(name);
        try {
            return settingsFolder.getCanonicalPath();
        }
        catch (IOException exception) {
            fail("Failed to create folder");
        }
        return null;
    }
    
    /**
     * Test that the application can be started, and a home directory chosen
     * by the user.
     * 
     * Expect that the main frame is shown and that the title
     * bar contains the chosen path.
     */
    @Test
    public void testStartingAndSelectingAHomeDirectory() {
        application.startWithNewSettingsStoredAt(aNewFolderCalled("mySettings"));

        final String myHomeFolderPath = aNewFolderCalled("myHomeDirectory");
        application.chooseHomeFolderOf(myHomeFolderPath);
        application.showsMainFrameWithTitleContaining(myHomeFolderPath);
    }
    
    /**
     * Test starting the application, and cancelling the selection of a
     * home directory.
     * 
     * Expect that the application exits without error.
     */
    @Test
    public void testStartingAndCancellingTheSelectionOfAHomeDirectory() {
        application.startWithNewSettingsStoredAt(aNewFolderCalled("mySettings"));

        application.cancelHomeFolderSelection();
        application.hasClosed();
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
        application.startWithNewSettingsStoredAt(settingsDirectoryPath);
        final String myHomeFolderPath = aNewFolderCalled("myHomeDirectory");
        application.chooseHomeFolderOf(myHomeFolderPath);
        application.showsMainFrameWithTitleContaining(myHomeFolderPath);
        application.exitTheApplication();
        
        // Re-launch the application with existing settings
        application.startWithExistingSettingsStoredAt(settingsDirectoryPath);
        application.showsMainFrameWithTitleContaining(myHomeFolderPath);
    }
}
