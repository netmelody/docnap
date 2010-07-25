package org.netmelody.docnap.core.testsupport.matcher;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public final class ZipFileMatcher extends TypeSafeMatcher<File> {

    private List<Matcher<? extends ZipEntry>> contents;

    public ZipFileMatcher(Matcher<? extends ZipEntry>... contents) {
        this.contents = Arrays.asList(contents);
    }
    
    @Override
    public void describeTo(Description description) {
        if (contents.isEmpty()) {
            description.appendText("an empty zip file ");
        }
        else {
            description.appendList("a zip file containing ", ", ", " ", contents);
        }
    }

    @Override
    public boolean matchesSafely(File zipFile) {
        List<Matcher<? extends ZipEntry>> toMatch = new ArrayList<Matcher<? extends ZipEntry>>(contents);
        
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(zipFile);
        }
        catch (FileNotFoundException e) {
            return false;
        }
        
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(fileInputStream));

        try {
            do {
                ZipEntry entry = zipInputStream.getNextEntry();
                if (entry == null) {
                    break;
                }

                if (entry.getCrc() == -1L) {
                    final CRC32 checkSum = new CRC32();
                    checkSum.update(IOUtils.toByteArray(zipInputStream));
                    entry.setCrc(checkSum.getValue());
                }
                
                if (!entryExpected(entry, toMatch)) {
                    return false;
                }
            } while (true);
        }
        catch (Exception e) {
            return false;
        }
        finally {
            try {
                zipInputStream.close();
            }
            catch (IOException e) {
                return false;
            }
        }
        
        return toMatch.isEmpty();
    }

    private boolean entryExpected(ZipEntry entry, List<Matcher<? extends ZipEntry>> toMatch) {
        Iterator<Matcher<? extends ZipEntry>> iterator = toMatch.iterator();
        while(iterator.hasNext()) {
            if (iterator.next().matches(entry)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static ZipFileMatcher isAnEmptyZipFile() {
        return new ZipFileMatcher();
    }
    
    @Factory
    public static Matcher<File> isAZipFileContaining(Matcher<? extends ZipEntry>... contents) {
        return new ZipFileMatcher(contents);
    }

    public static final class AZipEntryNamed extends TypeSafeMatcher<ZipEntry> {
        
        private final String name;

        public AZipEntryNamed(String name) {
            this.name = name;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("a Zip Entry named ");
            description.appendText(name);
            description.appendText(" ");
        }

        @Override
        public boolean matchesSafely(ZipEntry item) {
            return this.name.equals(item.getName());
        }
        
        @Factory
        public static AZipEntryNamed aZipEntryNamed(String name) {
            return new AZipEntryNamed(name);
        }
    }
    
    public static final class AZipEntryMatchingFile extends TypeSafeMatcher<ZipEntry> {

        private final File file;

        private AZipEntryMatchingFile(File file) {
            this.file = file;
        }
        
        @Override
        public void describeTo(Description description) {
            description.appendText("a Zip Entry ");
            description.appendValue(file);
            description.appendText(" ");
        }

        @Override
        public boolean matchesSafely(ZipEntry item) {
            if (!this.file.getName().equals(item.getName())) {
                return false;
            }
            try {
                return FileUtils.checksumCRC32(file) == item.getCrc();
            }
            catch (IOException e) {
                return false;
            }
        }

        @Factory
        public static AZipEntryMatchingFile aZipEntryMatchingFile(File file) {
            return new AZipEntryMatchingFile(file);
        }
    }
}
