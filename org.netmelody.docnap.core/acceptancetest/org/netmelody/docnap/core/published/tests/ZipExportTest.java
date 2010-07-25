package org.netmelody.docnap.core.published.tests;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertThat;
import static org.netmelody.docnap.core.testsupport.matcher.ZipFileMatcher.isAZipFileContaining;
import static org.netmelody.docnap.core.testsupport.matcher.ZipFileMatcher.isAnEmptyZipFile;
import static org.netmelody.docnap.core.testsupport.matcher.ZipFileMatcher.AZipEntryMatchingFile.aZipEntryMatchingFile;
import static org.netmelody.docnap.core.testsupport.matcher.ZipFileMatcher.AZipEntryNamed.aZipEntryNamed;

import java.io.File;
import java.util.zip.ZipEntry;

import org.junit.Test;
import org.netmelody.docnap.core.testsupport.DocnapCoreAcceptanceTest;

public class ZipExportTest extends DocnapCoreAcceptanceTest {

    @Test public void
    supportsExportingAnEmptyStore() {
        final File theZipFile = given().aNewEmptyFile();
        when().aRequestIsMadeTo().exportToAZipFile(theZipFile);
        
        assertThat(theZipFile, isAnEmptyZipFile());
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    supportsExportingASingleDocument() {
        final File theZipFile = given().aNewEmptyFile();
        givenAStore().containingOneDocument();
        
        when().aRequestIsMadeTo().exportToAZipFile(theZipFile);
        
        assertThat(theZipFile, isAZipFileContaining(any(ZipEntry.class)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    supportsExportingTwoDocuments() {
        File file1 = given().aNewPopulatedFile();
        File file2 = given().aNewPopulatedFile();
        givenAStore().containingADocumentFor(file1)
               .and().containingADocumentFor(file2);
        
        final File theZipFile = given().aNewEmptyFile();
        
        when().aRequestIsMadeTo().exportToAZipFile(theZipFile);
        assertThat(theZipFile, isAZipFileContaining(aZipEntryMatchingFile(file1), aZipEntryMatchingFile(file2)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    supportsExportingTwoDocumentsWithTheSameName() {
        final String filenameWithoutExtension = given().aFileNameWithoutExtension();
        final String extension = given().aFileExtension();
        final String filename = filenameWithoutExtension + extension;
        
        File file = given().aNewPopulatedFileCalled(filename);
        givenAStore().containingADocumentFor(file)
               .and().containingADocumentFor(file);
        
        final File theZipFile = given().aNewEmptyFile();
        
        when().aRequestIsMadeTo().exportToAZipFile(theZipFile);
        
        assertThat(theZipFile,
                   isAZipFileContaining(aZipEntryNamed(filename),
                                        aZipEntryNamed(filenameWithoutExtension + "_1" + extension)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    supportsExportingTwoDocumentsWithSameNameWhenTheIntendedRenameAlsoExists() {
        final String filenameWithoutExtension = given().aFileNameWithoutExtension();
        final String extension = given().aFileExtension();
        final String filename = filenameWithoutExtension + extension;
        
        File file = given().aNewPopulatedFileCalled(filename);
        File file2 = given().aNewPopulatedFileCalled(filenameWithoutExtension + "_1" + extension);
        givenAStore().containingADocumentFor(file)
               .and().containingADocumentFor(file)
               .and().containingADocumentFor(file2);
        
        final File theZipFile = given().aNewEmptyFile();
        
        when().aRequestIsMadeTo().exportToAZipFile(theZipFile);
        
        assertThat(theZipFile,
                   isAZipFileContaining(aZipEntryNamed(filename),
                                        aZipEntryNamed(filenameWithoutExtension + "_1" + extension),
                                        aZipEntryNamed(filenameWithoutExtension + "_2" + extension)));
    }
    
}
