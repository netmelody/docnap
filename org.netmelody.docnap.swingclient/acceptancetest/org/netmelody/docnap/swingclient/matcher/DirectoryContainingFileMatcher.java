package org.netmelody.docnap.swingclient.matcher;

import java.io.File;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class DirectoryContainingFileMatcher extends DirectoryMatcher {
    
    private final String fileName;

    public DirectoryContainingFileMatcher(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public boolean matchesSafely(String directory) {
        final File file = new File(directory);
        if (!(super.matchesSafely(directory) && file.list().length > 0)) {
            return false;
        }
        final File content = new File(directory + File.separator + this.fileName);
        return content.isFile();
    }

    public void describeTo(Description description) {
        description.appendText("is a directory containing " + this.fileName);
    }
    
    @Factory
    public static Matcher<String> aDirectoryContaining(String fileName) {
        return new DirectoryContainingFileMatcher(fileName);
    }

}
