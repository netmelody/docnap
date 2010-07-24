package org.netmelody.docnap.core.testsupport.matcher;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public class ThePathToAPopulatedDirectory extends TypeSafeMatcher<String> {
    @Override
    public boolean matchesSafely(String path) {
        return !FileUtils.listFiles(new File(path), null, false).isEmpty();
    }

    public void describeTo(Description description) {
        description.appendText("not the path to a populated directory");
    }

    @Factory
    public static <T> Matcher<String> thePathToAPopulatedDirectory() {
        return new ThePathToAPopulatedDirectory();
    }
}