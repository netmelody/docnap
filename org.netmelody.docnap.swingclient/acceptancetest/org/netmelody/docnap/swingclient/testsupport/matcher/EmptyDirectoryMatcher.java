package org.netmelody.docnap.swingclient.testsupport.matcher;

import java.io.File;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class EmptyDirectoryMatcher extends DirectoryMatcher {

    @Override
    public boolean matchesSafely(String directory) {
        final File file = new File(directory);
        return super.matchesSafely(directory) && (0 == file.list().length); 
    }

    public void describeTo(Description description) {
        description.appendText("is an empty directory");
    }
    
    @Factory
    public static Matcher<String> anEmptyDirectory() {
        return new EmptyDirectoryMatcher();
    }
}
