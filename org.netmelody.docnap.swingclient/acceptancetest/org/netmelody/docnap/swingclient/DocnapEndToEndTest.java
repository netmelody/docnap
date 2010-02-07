package org.netmelody.docnap.swingclient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Framework for end-to-end testing of the Docnap Swing user interface.
 * 
 * @author Tom Denley
 *
 */
public class DocnapEndToEndTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private final DocnapApplicationRunner application = new DocnapApplicationRunner();
    
    @Before
    public final void checkTemporaryFolderExists() throws IOException {
        assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
    }

    @After
    public final void stopTheApplication() {
        application.stop();
    }

    public final String aNewFolderCalled(final String name) {
        final File settingsFolder = this.folder.newFolder(name);
        try {
            return settingsFolder.getCanonicalPath();
        }
        catch (IOException exception) {
            fail("Failed to create folder");
        }
        return null;
    }
    
    public final DocnapApplicationRunner docnap() {
        return application;
    }
    
    public final void startTheApplication() {
        application.startWithNewSettingsStoredAt(aNewFolderCalled("mySettings"));
        application.chooseHomeFolderOf(aNewFolderCalled("myHomeDirectory"));
    }
}
