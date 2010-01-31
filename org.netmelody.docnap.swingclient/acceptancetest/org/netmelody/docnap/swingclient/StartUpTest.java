package org.netmelody.docnap.swingclient;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.published.Bootstrap;
import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.picocontainer.PicoContainer;

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
        DocnapStoreChooserDriver driver = new DocnapStoreChooserDriver(applicationDriver);
        driver.enterDirectory(settingsFolderPath);
    }

    @After
    public void stopTheApplication() {
        applicationDriver.dispose();
    }

    @Test
    public void testCheckingInADocumentAndGettingItOutAgain() {
        applicationDriver.clickTheIndexFileButtonOnTheToolBar();
    }
}
