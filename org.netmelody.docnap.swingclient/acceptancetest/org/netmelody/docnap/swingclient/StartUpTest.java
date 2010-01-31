package org.netmelody.docnap.swingclient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.not;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static com.objogate.wl.swing.driver.ComponentDriver.showingOnScreen;

public class StartUpTest {

    private DocnapApplicationDriver applicationDriver;
    
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void runTheApplication() throws IOException {
        assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
        
        final File settingsFolder = this.folder.newFolder("mySettings");
        final String settingsFolderPath = settingsFolder.getCanonicalPath();
        
        DocnapMain.main(new String[]{settingsFolderPath});
        applicationDriver = new DocnapApplicationDriver();
        applicationDriver.is(not(showingOnScreen()));
    }

    @After
    public void stopTheApplication() {
        applicationDriver.dispose();
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
        final File homeFolder = this.folder.newFolder("myHomeDirectory");
        final String homeFolderPath = homeFolder.getCanonicalPath();
        
        final DocnapStoreChooserDriver driver = new DocnapStoreChooserDriver(applicationDriver);
        driver.enterDirectory(homeFolderPath);
        applicationDriver.is(showingOnScreen());
        
        //TODO: Add expectation that the title bar contains the chosen path.
    }
    
    /**
     * Test starting the application, and cancelling the selection of a
     * home directory.
     * 
     * Expect that the application exits without error.
     */
    @Test
    public void testStartingAndCancellingTheSelectionOfAHomeDirectory() {
        final DocnapStoreChooserDriver driver = new DocnapStoreChooserDriver(applicationDriver);
        driver.cancel();
        
        applicationDriver.is(not(showingOnScreen()));
        
        //TODO: Assert that the application has terminated.
    }
    
    /**
     * Test starting the application a second time, pointing at the same
     * settings directory, and hence picking up the same home directory as
     * before.
     * 
     * @throws IOException bad
     */
    @Test
    @Ignore
    public void testStartingWithHomeDirectoryRemembered() throws IOException {

    }
}
