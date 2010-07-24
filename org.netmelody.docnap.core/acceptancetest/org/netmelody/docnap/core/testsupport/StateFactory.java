package org.netmelody.docnap.core.testsupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;

public class StateFactory {
    
    private final TemporaryFolder folder;
    private int storeNumber = 0;
    private int documentNumber = 0;
    private int documentContentsNumber = 0;
    private int fileNameNumber = 0;
    private int tagNumber = 0;
    
    public StateFactory(TemporaryFolder folder) {
        this.folder = folder;        
    }
    
    public String thePathToANewFolderForADocnapStore() {
        try {
            return folder.newFolder("Store" + storeNumber++).getCanonicalPath();
        }
        catch (IOException e) {
            fail("failed to create new folder");
            return "";
        }
    }
    
    /**
     * Creates a new docnap store in a folder called mystore inside
     * the temporary folder for the test.
     * Returns the context of the newly created doc nap store.
     * 
     * @return PicoContainer
     */
    public DocnapCoreDriver aNewDocNapStore() {
        return openDocNapStore(thePathToANewFolderForADocnapStore());
    }
    
    public DocnapCoreDriver aNewDocNapStoreLocatedAt(String path) {
        assertThat("Directory for a new store must be empty",
                   FileUtils.listFiles(new File(path), null, true).size(), is(0));
        return openDocNapStore(path);
    }

    public DocnapCoreDriver aReopenedDocnapStore(String storeLocation) throws IOException{
        return openDocNapStore(storeLocation);
    }
    
    /**
     * Opens a docnap store in the folder specified inside
     * the temporary folder for the test.
     * This with create a new store if it doesn't already exist
     * 
     * Returns the context of the doc nap store.
     * 
     * @return PicoContainer
     */
    private DocnapCoreDriver openDocNapStore(String storeLocation) {
        final DocnapCoreDriver docnap = new DocnapCoreDriver(this);
        docnap.setStorageLocation(storeLocation);
        
        return docnap;
    }
    
    /*
     * Files
     */
    public File aNewDocumentFile() throws IOException {
        return aNewDocumentFileCalled(aDocumentName());
    }
    
    public File aNewDocumentFileCalled(String fileName) throws IOException {
        return aNewDocumentFileCalledWithContents(fileName, theContentsOfADocument());
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
    
    public File aNewDocumentFileCalledWithContents(String fileName, String fileContents) throws IOException{
        assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
        
        final File newFile = this.folder.newFile(fileName);
        final BufferedWriter out = new BufferedWriter(new FileWriter(newFile));
        out.write(fileContents);
        out.close();
        
        assertThat("Incorrect test file setup.", FileUtils.readFileToString(newFile), is(fileContents));
     
        return newFile;
    }
    
    public File aNewEmptyFile() {
        return aNewEmptyFileCalled(aFileName());
    }
    
    public ArrayList<File> nNewDocumentFiles(int n) throws IOException {
        final ArrayList<File> files = new ArrayList<File>();
        
        for (int fileNumber = 0; fileNumber < n; fileNumber++) {
            files.add(aNewDocumentFile());
        }
        
        return files;
    }

    public String aDocumentName() {
        return "document name" + documentNumber++;
    }
    
    public String aFileName() {
        return "fileName" + fileNameNumber++;
    }
    
    public String theContentsOfADocument() {
        return "contents " + documentContentsNumber++;
    }
    
    public String aTagTitle() {
        return "tag title " + tagNumber++;
    }
}
