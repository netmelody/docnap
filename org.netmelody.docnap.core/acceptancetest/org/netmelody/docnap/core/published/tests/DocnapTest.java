package org.netmelody.docnap.core.published.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.netmelody.docnap.core.published.tests.DocnapTest.ThePathToAPopulatedDirectory.thePathToAPopulatedDirectory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.testsupport.StateFactory;
import org.netmelody.docnap.core.testsupport.checker.DocnapCoreChecker;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;

public class DocnapTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();
    
    private final StateFactory stateFactory = new StateFactory(folder);

    private DocnapCoreDriver docnap;
    
    @Test
    public void supportsCreatingANewDocnapStoreWithStorageLocation() {
        final String path = given().thePathToANewFolderForADocnapStore();
        docnap = given().aNewDocNapStoreLocatedAt(path);
        
        then().theStore().isEmpty();
        assertThat(path, is(thePathToAPopulatedDirectory()));
    }
    
    @Test
    public void supportsReopeningAnExistingStore() {
        final String path = given().thePathToANewFolderForADocnapStore();
        docnap = given().aNewDocNapStoreLocatedAt(path).containingOneDocument();
        
        when().theStoreIsReopened();
        
        then().theStore().hasOneDocument();
    }
    
    public static class ThePathToAPopulatedDirectory extends TypeSafeMatcher<String> {
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
    
    private StateFactory given() {
        return this.stateFactory;
    }

    private DocnapCoreDriver when() {
        return this.docnap;
    }
    
    private DocnapCoreChecker then() {
        return new DocnapCoreChecker(this.docnap, this.stateFactory);
    }
}
