package org.netmelody.docnap.swingclient.testsupport.matcher;

import java.io.File;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class DirectoryMatcher extends TypeSafeMatcher<String> {

    @Override
    public boolean matchesSafely(String directory) {
        final File file = new File(directory);
        return file.isDirectory(); 
    }

    public void describeTo(Description description) {
        description.appendText("is a directory");
    }
    
    @Factory
    public static Matcher<String> aDirectory() {
        return new DirectoryMatcher();
    }
}