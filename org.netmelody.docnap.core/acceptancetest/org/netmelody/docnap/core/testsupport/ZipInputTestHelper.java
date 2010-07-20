package org.netmelody.docnap.core.testsupport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.rules.TemporaryFolder;

public class ZipInputTestHelper {
    
    private ZipInputTestHelper() {
        super();
    }
    
    private static ArrayList<File> getListOfZippedFiles(File zipFile, TemporaryFolder folder) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(zipFile);
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));
        
        ArrayList<File> zippedFiles = new ArrayList<File>();
        File zipFolder = folder.newFolder("zipFolder");
          
        do {
            ZipEntry entry = zipInputStream.getNextEntry();
            if (entry == null)
                break;
            
            File unzippedFile = new File(zipFolder, entry.getName());
            FileOutputStream fileOutputStream = new FileOutputStream(unzippedFile);
               
            IOUtils.copy(zipInputStream, fileOutputStream);
            
            zippedFiles.add(unzippedFile);
        } while (1 == 1);
        
        zipInputStream.close();
        
        return zippedFiles;
        
    }
    
    private static void checkFile(File file, String fileName, String fileContent) throws IOException {
        
        assertEquals("File name not correct", fileName, file.getName());
        assertThat("Incorrect file content.", FileUtils.readFileToString(file), is(fileContent));
    }
            
    
    public static void checkZipFile(File zipFile, TemporaryFolder folder,
                                    String[] fileNames, String[] fileContent) throws IOException {
        ArrayList<File> zippedFiles = getListOfZippedFiles(zipFile, folder);
        
        Collections.sort(zippedFiles, new FileCompare());   
        
        assertEquals("zip file doesn't contain the right number of files", fileNames.length, zippedFiles.size());
        
        int fileIndex = 0;
        for (File file : zippedFiles) {
            checkFile(file, fileNames[fileIndex], fileContent[fileIndex]);
            fileIndex++;
        }
        
    }
    
    private static class FileCompare implements Comparator<File> {

        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(File file1, File file2) {

            return file1.getName().compareTo(file2.getName());
        }
        
    }

}
