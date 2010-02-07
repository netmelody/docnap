package org.netmelody.docnap.swingclient;

import static com.objogate.wl.swing.driver.ComponentDriver.showingOnScreen;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.netmelody.docnap.swingclient.matcher.EmptyDirectoryMatcher.anEmptyDirectory;
import static org.netmelody.docnap.swingclient.matcher.DirectoryContainingFileMatcher.aDirectoryContaining;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.jdesktop.application.Application;

public final class DocnapApplicationRunner {

    private DocnapApplicationDriver applicationDriver;
    private boolean hasErrored = false;
    
    public void startDocnapApplicationWithNewSettingsStoredAt(final String settingsDirectoryPath) {
        stop();
        assertThat(settingsDirectoryPath, is(anEmptyDirectory()));
        captureLogging();
        DocnapMain.main(new String[]{settingsDirectoryPath});
        applicationDriver = DocnapApplicationDriver.docnapApplicationNotShowingMainFrameOnScreen();
    }
    
    public void startDocnapApplicationWithExistingSettingsStoredAt(final String settingsDirectoryPath) {
        stop();
        assertThat(settingsDirectoryPath, is(aDirectoryContaining("lasthome.xml")));
        captureLogging();
        DocnapMain.main(new String[]{settingsDirectoryPath});
        applicationDriver = DocnapApplicationDriver.docnapApplicationShowingMainFrameOnScreen();
    }

    public void stop() {
       if (null != this.applicationDriver) {
           this.applicationDriver.dispose();
       }
    }

    private void captureLogging() {
        LogManager logManager = LogManager.getLogManager();
        logManager.reset();

        Handler handler = new Handler() {
            @Override
            public void close() throws SecurityException {}
            @Override
            public void flush() {}
            @Override
            public void publish(LogRecord record) {
                if (record.getLevel().equals(Level.SEVERE)) {
                    DocnapApplicationRunner.this.hasErrored = true;
                }
            }
        };
        handler.setFormatter(new SimpleFormatter());
        Logger.getLogger(Application.class.getName()).addHandler(handler);
    }
    
    
    // Actions performed on the application
    
    public void chooseHomeFolderOf(String myHomeFolderPath) {
        final DocnapStoreChooserDriver driver = new DocnapStoreChooserDriver(applicationDriver);
        driver.enterDirectory(myHomeFolderPath);
    }

    public void cancelHomeFolderSelection() {
        final DocnapStoreChooserDriver driver = new DocnapStoreChooserDriver(applicationDriver);
        driver.cancel();
    }

    public void exitTheApplication() {
        applicationDriver.clickTheFileExitMenuOption();
    }

    
    //Assertions of application state
    
    public void showsMainFrameWithTitleContaining(String myHomeFolderPath) {
        applicationDriver.is(showingOnScreen());
        applicationDriver.hasTitle(containsString(myHomeFolderPath));
    }

    public void hasClosed() {
        assertThat(hasErrored, is(false)); 
    }

}