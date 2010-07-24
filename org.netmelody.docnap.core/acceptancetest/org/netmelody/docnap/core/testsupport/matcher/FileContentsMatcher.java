package org.netmelody.docnap.core.testsupport.matcher;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.internal.matchers.TypeSafeMatcher;

public class FileContentsMatcher extends TypeSafeMatcher<File> {

    private final File target;

    public static Matcher<File> hasContentsEqualTo(File file) {
        return new FileContentsMatcher(file);
    }
    
    public FileContentsMatcher(File target) {
        this.target = target;
    }

    @Override
    public void describeTo(Description desc) {
        desc.appendText(" has contents matching " + target.getName());
    }

    @Override
    public boolean matchesSafely(File item) {
        try {
            return FileUtils.contentEquals(target, item);
        }
        catch (IOException e) {
            return false;
        }
    }
}
