package org.netmelody.docnap.core.testsupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;

public class StateFactory {
    
    private final TemporaryFolder folder;
    
    private DocnapCoreDriver docnap;
    private int sequence = 0;
    
    public StateFactory(TemporaryFolder folder) {
        this.folder = folder;        
    }
    
    public String thePathToAnEmptyFolder() {
        try {
            return folder.newFolder("Store" + sequence++).getCanonicalPath();
        }
        catch (IOException e) {
            fail("failed to create new folder");
            return "";
        }
    }
    
    public DocnapCoreDriver aDocnapStore() {
        return this.docnap;
    }
    
    public DocnapCoreDriver aNewDocNapStore() {
        return openDocNapStore(thePathToAnEmptyFolder());
    }
    
    public DocnapCoreDriver aNewDocNapStoreLocatedAt(String path) {
        assertThat("Directory for a new store must be empty",
                   FileUtils.listFiles(new File(path), null, true).size(), is(0));
        return openDocNapStore(path);
    }

    public DocnapCoreDriver aReopenedDocnapStore(String path) {
        assertThat("Directory for an existing store must not be empty",
                FileUtils.listFiles(new File(path), null, false).size(), is(not(0)));
        return openDocNapStore(path);
    }
    
    private DocnapCoreDriver openDocNapStore(String storeLocation) {
        this.docnap = new DocnapCoreDriver(this);
        this.docnap.setStorageLocation(storeLocation);
        
        return this.docnap;
    }
    
    public File aNewPopulatedFile() {
        return aNewPopulatedFileCalled(aFileName());
    }
    
    public File aNewPopulatedFileCalled(String name) {
        final File result = aNewEmptyFileCalled(name);
        //TODO: Randomise this
        final String randomText = "aliwrjgoigfsoudhgzkufohgoiu";
        try {
            FileUtils.writeStringToFile(result, randomText);
        }
        catch (IOException e) {
            fail("Unable to create a new file called" + name);
        }
        return result;
    }

    public File aNewEmptyFileCalled(String name) {
        try {
            return this.folder.newFile(name);
        }
        catch (IOException e) {
            fail("Unable to create a new empty file called" + name);
        }
        return null;
    }
    
    public File aNewEmptyFile() {
        return aNewEmptyFileCalled(aFileName());
    }

    public String aDocumentTitle() {
        return "document name" + sequence++;
    }
    
    public String aFileName() {
        return "fileName" + sequence++;
    }
    
    public String aTagTitle() {
        return "tag title " + sequence++;
    }
}
