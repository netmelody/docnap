package org.netmelody.docnap.core.published.testsupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.published.Bootstrap;
import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.published.testsupport.driver.DocnapCoreDriver;
import org.picocontainer.PicoContainer;

public class DocnapFactory {
    
    private final TemporaryFolder folder;
    private int storeNumber = 0;
    private int documentNumber = 0;
    private int documentContentsNumber = 0;
    private int fileNameNumber = 0;
    private int tagNumber = 0;
    
    public DocnapFactory(TemporaryFolder folder) {
        this.folder = folder;        
    }
    
    public String thePathToANewFolderForADocnapStore() throws IOException{
        return folder.newFolder("Store" + storeNumber++).getCanonicalPath();
    }
    
    /**
     * Creates a new docnap store in a folder called mystore inside
     * the temporary folder for the test.
     * Returns the context of the newly created doc nap store.
     * 
     * @return PicoContainer
     */
    public DocnapCoreDriver aNewDocNapStore() throws IOException {
        return openDocNapStore(thePathToANewFolderForADocnapStore());
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
    private DocnapCoreDriver openDocNapStore(String storeLocation) throws IOException {
        final PicoContainer context = new Bootstrap().start();
        final IDocnapStore store = context.getComponent(IDocnapStore.class);
        
        store.setStorageLocation(storeLocation);
        
        return new DocnapCoreDriver(context, this);
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
    
    public File aNewDocumentFileCalledWithContents(String fileName, String fileContents) throws IOException{
        assertThat("TemporaryFolder root incorrect.", this.folder.getRoot(), is(notNullValue()));
        
        final File newFile = this.folder.newFile(fileName);
        final BufferedWriter out = new BufferedWriter(new FileWriter(newFile));
        out.write(fileContents);
        out.close();
        
        assertThat("Incorrect test file setup.", FileUtils.readFileToString(newFile), is(fileContents));
     
        return newFile;
    }
    
    public File aNewEmptyFile() throws IOException {
        return this.folder.newFile(aFileName());
    }
    
    public ArrayList<File> nNewDocumentFiles(int n) throws IOException {
        final ArrayList<File> files = new ArrayList<File>();
        
        for (int fileNumber = 0; fileNumber < n; fileNumber++) {
            files.add(aNewDocumentFile());
        }
        
        return files;
    }
    
    /*
     * Test Documents
     */
    public TestDocument aNewTestDocument() throws IOException {
        return new TestDocument(aNewDocumentFile());
    }
    
    /*
     * Tags
     */
    public ArrayList<String> nTagTitles(int n) {
        final ArrayList<String> tagTitles = new ArrayList<String>();
        
        for (int fileNumber = 0; fileNumber < n; fileNumber++) {
            tagTitles.add(aTagTitle());
        }
        
        return tagTitles;
    }
    
    
    /*
     * Document properties
     */
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
