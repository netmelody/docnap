package org.netmelody.docnap.core.repository;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

public class DocZipOutput {
	
	private final ZipOutputStream zipOutput;
	private final HashSet<String> filenameMap = new HashSet<String>();

	public DocZipOutput(File zipFile) throws IOException {
		final FileOutputStream output = new FileOutputStream(zipFile);
        zipOutput = new ZipOutputStream(new BufferedOutputStream(output));
	}
	
	public void addDocument(File document, String toZipFileName) throws IOException {
	    String fileZipToFilename = getUniqueFilename(toZipFileName);
	    
		final FileInputStream input = new FileInputStream(document);
       
        final ZipEntry zipEntry = new ZipEntry(fileZipToFilename);
        zipOutput.putNextEntry(zipEntry);
        
        IOUtils.copy(input, zipOutput);
        
        zipOutput.closeEntry();
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
