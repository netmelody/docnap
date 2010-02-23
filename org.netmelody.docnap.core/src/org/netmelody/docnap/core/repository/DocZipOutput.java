package org.netmelody.docnap.core.repository;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DocZipOutput {
	
    private static final int FILE_INPUT_BUFFER_SIZE = 2048;
	
	private final ZipOutputStream zipOutput;
	private final HashSet<String> filenameMap = new HashSet<String>();

	public DocZipOutput(File zipFile) throws IOException {
		final FileOutputStream output = new FileOutputStream(zipFile);
        zipOutput = new ZipOutputStream(new BufferedOutputStream(output));
	}
	
	public void addDocument(File document, String toZipFileName) throws IOException {
	    String fileZipToFilename = getUniqueFilename(toZipFileName);
	    
	    
	    
		final byte data[] = new byte[FILE_INPUT_BUFFER_SIZE];
		final FileInputStream input = new FileInputStream(document);
        final BufferedInputStream fileInStream = new BufferedInputStream(input, FILE_INPUT_BUFFER_SIZE);
        
        final ZipEntry zipEntry = new ZipEntry(fileZipToFilename);
        zipOutput.putNextEntry(zipEntry);
        
        do {
      	  final int count = fileInStream.read(data, 0, FILE_INPUT_BUFFER_SIZE);
      	  if (count == -1) {
      		  break;
      	  }
      	  zipOutput.write(data, 0, FILE_INPUT_BUFFER_SIZE);
        } while (1==1);
        
        zipOutput.closeEntry();
        fileInStream.close();
	}
	
	private String getUniqueFilename(String originalFilename) {
	    final int dotPosition = originalFilename.lastIndexOf('.');
	    final String fileExtensionWithDot = (-1 == dotPosition) ? "" : originalFilename.substring(dotPosition);
	    final String originalName = (-1 == dotPosition) ? originalFilename : originalFilename.substring(0, dotPosition);
	    
	    String fileName = originalName;
	    int counter = 0;

	    while(filenameMap.contains(fileName + fileExtensionWithDot)) {
	        counter++;
	        fileName = originalName + "_" + counter;
	    }
	    
	    filenameMap.add(fileName + fileExtensionWithDot);
	    return fileName + fileExtensionWithDot;
	    
	}
	
	public void close() throws IOException {
		zipOutput.close();
	}
}
