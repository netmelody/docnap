package org.netmelody.docnap.swingclient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.fail;
import org.junit.rules.TemporaryFolder;

public class StartUpTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private final DocnapApplicationRunner application = new DocnapApplicationRunner();
    
    @Before
    public void runTheApplication() throws IOException {
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
     * by the user. Expect that the main frame is shown and that the title
     * bar contains the chosen path.
     * 
     * @throws IOException bad
     */
    @Test
    public void testStartingAndSelectingAHomeDirectory() throws IOException {
        application.startDocnapApplicationWithNewSettingsStoredAt(aNewFolderCalled("mySettings"));

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
        application.startDocnapApplicationWithNewSettingsStoredAt(aNewFolderCalled("mySettings"));

        application.cancelHomeFolderSelection();
        application.hasClosed();
    }
    
    /**
     * Test starting the application a second time, pointing at the same
     * settings directory, and hence picking up the same home directory as
     * before.
     * 
     * @throws IOException bad
     */
    @Test
    public void testStartingWithHomeDirectoryRemembered() throws IOException {
        final String settingsDirectoryPath = aNewFolderCalled("mySettings");
        application.startDocnapApplicationWithNewSettingsStoredAt(settingsDirectoryPath);
        final String myHomeFolderPath = aNewFolderCalled("myHomeDirectory");
        application.chooseHomeFolderOf(myHomeFolderPath);
        application.showsMainFrameWithTitleContaining(myHomeFolderPath);
        application.exitTheApplication();
        
        application.startDocnapApplicationWithExistingSettingsStoredAt(settingsDirectoryPath);
        application.showsMainFrameWithTitleContaining(myHomeFolderPath);
    }
}
