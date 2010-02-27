package org.netmelody.docnap.swingclient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
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

    public final String theFullPathToANewFolderCalled(final String name) {
        final File settingsFolder = this.folder.newFolder(name);
        try {
            return settingsFolder.getCanonicalPath();
        }
        catch (IOException exception) {
            fail("Failed to create folder");
        }
        return null;
    }
    
    public final String theFullPathToANewPopulatedFileCalled(String name) {
        final String result = theFullPathToANewEmptyFileCalled(name);
        //TODO: Randomise this
        final String randomText = "aliwrjgoigfsoudhgzkufohgoiu";
        try {
            FileUtils.writeStringToFile(new File(result), randomText);
        }
        catch (IOException e) {
            fail("Unable to create a new file called" + name);
        }
        return result;
    }
    
    public final String theFullPathToANewEmptyFileCalled(String name) {
        try {
            return this.folder.newFile(name).getCanonicalPath();
        }
        catch (IOException e) {
            fail("Unable to create a new empty file called" + name);
        }
        return null;
    }
    
    public final String theFullPathToNewNonExistantFileCalled(String name) {
        try {
            final String result = this.folder.newFile(name).getCanonicalPath();
            FileUtils.forceDelete(new File(result));
            return result;
        }
        catch (IOException e) {
            fail("Unable to devise a new non-existant file named " + name);
        }
        return null;
    }
    
    public final DocnapApplicationRunner theUserTriesTo() {
        return application;
    }
    
    public final void startTheApplication() {
        application.startWithNewSettingsStoredAt(theFullPathToANewFolderCalled("mySettings"));
        application.chooseHomeFolderOf(theFullPathToANewFolderCalled("myHomeDirectory"));
    }
}
