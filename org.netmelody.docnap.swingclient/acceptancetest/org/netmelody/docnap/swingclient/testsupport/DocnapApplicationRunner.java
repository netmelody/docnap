package org.netmelody.docnap.swingclient.testsupport;

import static com.objogate.wl.swing.driver.ComponentDriver.showingOnScreen;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.netmelody.docnap.swingclient.testsupport.matcher.DirectoryContainingFileMatcher.aDirectoryContaining;
import static org.netmelody.docnap.swingclient.testsupport.matcher.EmptyDirectoryMatcher.anEmptyDirectory;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.hamcrest.Description;
import org.jdesktop.application.Application;
import org.netmelody.docnap.swingclient.DocnapMain;
import org.netmelody.docnap.swingclient.testsupport.driver.DocnapApplicationDriver;
import org.netmelody.docnap.swingclient.testsupport.driver.DocnapDocumentDetailsFrameDriver;
import org.netmelody.docnap.swingclient.testsupport.driver.DocnapStoreChooserDriver;

import com.objogate.wl.Probe;
import com.objogate.wl.UnsynchronizedProber;

public final class DocnapApplicationRunner {

    private DocnapApplicationDriver applicationDriver;
    private boolean hasErrored = false;
    
    public void startDocnapWithNewSettingsStoredAt(final String settingsDirectoryPath) {
        stop();
        assertThat(settingsDirectoryPath, is(anEmptyDirectory()));
        captureLogging();
        DocnapMain.main(new String[]{settingsDirectoryPath});
        applicationDriver = DocnapApplicationDriver.docnapApplicationNotShowingMainFrameOnScreen();
    }
    
    public void startDocnapWithExistingSettingsStoredAt(final String settingsDirectoryPath) {
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
    
    public void chooseAHomeFolderOf(String myHomeFolderPath) {
        final DocnapStoreChooserDriver driver = new DocnapStoreChooserDriver(applicationDriver);
        driver.enterDirectory(myHomeFolderPath);
    }

    public void chooseANewHomeFolderOf(String myHomeFolderPath) {
        applicationDriver.clickTheFileSelectHomeDirectoryMenuOption();
        chooseAHomeFolderOf(myHomeFolderPath);
    }

    public void cancelHomeFolderSelection() {
        final DocnapStoreChooserDriver driver = new DocnapStoreChooserDriver(applicationDriver);
        driver.cancel();
    }

    public void exitTheApplication() {
        applicationDriver.clickTheFileExitMenuOption();
    }

    public DocnapDocumentDetailsFrameDriver indexANewFileCalled(String filename) {
        applicationDriver.clickTheIndexFileButtonOnTheToolBar();
        
        final DocnapDocumentDetailsFrameDriver detailsFrameDriver = new DocnapDocumentDetailsFrameDriver(applicationDriver);
        detailsFrameDriver.indexFileCalled(filename);
        
        return detailsFrameDriver;
    }
    
    public DocnapDocumentDetailsFrameDriver selectTheAllTagAndViewDetailsOfTheDocumentTitled(String title) {
        return selectTheTagCalled("All").andViewDetailsofDocumentTitled(title);
    }
    
    public DocnapApplicationRunner selectTheTagCalled(String tagTitle) {
        applicationDriver.selectTheTagCalled(tagTitle);
        return this;
    }
    
    public DocnapDocumentDetailsFrameDriver andViewDetailsofDocumentTitled(String title) {
        applicationDriver.selectTheDocumentTitled(title);
        applicationDriver.clickTheShowFileButtonOnTheToolBar();
        return new DocnapDocumentDetailsFrameDriver(applicationDriver);
    }
    
    //Assertions of application state
    
    public void showsMainFrameWithTitleContaining(String myHomeFolderPath) {
        applicationDriver.is(showingOnScreen());
        applicationDriver.hasTitle(containsString(myHomeFolderPath));
    }

    public DocnapDocumentDetailsFrameDriver showsDocumentDetailsForADocumentTitled(String title) {
        return new DocnapDocumentDetailsFrameDriver(applicationDriver, title);
    }
    
    private boolean hasFinished(ThreadGroup group) 
    {
        int numThreads = group.activeCount(); 
        Thread[] threads = new Thread[numThreads*2]; 
        numThreads = group.enumerate(threads, false); 

        for (int i=0; i<numThreads; i++) { 
            // Get thread 
            Thread thread = threads[i]; 
            if (thread.getName().startsWith("AWT-EventQueue")) {
                if (Thread.State.WAITING == thread.getState()) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        
        return true;
    }

    public void hasClosed() {
        UnsynchronizedProber checkFinished = new UnsynchronizedProber();
        
        checkFinished.check(new Probe() {
            
            @Override
            public void describeTo(Description description) {
            }
            
            @Override
            public void probe() {
            }
            
            @Override
            public boolean isSatisfied() {
                ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                return hasFinished(threadGroup);
                
            }
            
            @Override
            public void describeFailureTo(Description description) {
                
            }
        });
        
        assertThat(hasErrored, is(false)); 
    }

    public void hasNoDocumentTitled(String documentTitle) {
        applicationDriver.hasNoDocumentTitled(documentTitle);
    }
    
    public void hasNoTagTitled(String tagTitle) {
        applicationDriver.hasNoTagTitled(tagTitle);
    }
    
    public void hasDocumentTitled(String documentTitle) {
        applicationDriver.hasDocumentTitled(documentTitle);
    }
    
    public void hasTagTitled(String tagTitle) {
        applicationDriver.hasTagTitled(tagTitle);
    }
}