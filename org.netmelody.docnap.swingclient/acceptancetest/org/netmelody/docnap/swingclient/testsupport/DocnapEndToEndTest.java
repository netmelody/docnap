package org.netmelody.docnap.swingclient.testsupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Framework for end-to-end testing of the Docnap Swing user interface.
 * 
 * <p>Does not automatically start the application. Call startTheApplication to
 * launch a new docnap instance in a clean directory. Use richer startup
 * methods on the docnap() driver for different start-up scenarios.</p>
 * 
 * <p>Automatically disposes of the application it at tear-down.</p>
 * 
 * @author Tom Denley
 *
 */
public class DocnapEndToEndTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    
    private final StateFactory stateFactory = new StateFactory(this.folder);
    private final DocnapApplicationRunner application = new DocnapApplicationRunner();
    
    @Before
    public final void checkTemporaryFolderExists() throws IOException {
        assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
    }

    /**
     * Start the application in a new, clean home directory. This is not done @Before by default
     * because some tests might want to start the application differently.
     */
    public final void startTheApplication() {
        final String settings = given().theFullPathToANewSettingsDirectory();
        final String home = given().theFullPathToANewHomeDirectory();
        
        application.startDocnapWithNewSettingsStoredAt(settings);
        application.chooseAHomeFolderOf(home);
    }

    @After
    public final void stopTheApplication() {
        application.stop();
    }

    public final StateFactory given() {
        return this.stateFactory ;
    }
    
    /**
     * Use when writing actions performed by the user.
     * @return
     */
    public final DocnapApplicationRunner theUserTriesTo() {
        return application;
    }
    
    /**
     * Use when asserting docnap application state.
     * @return
     */
    public final DocnapApplicationRunner docnap() {
        return application;
    }
}
