package org.netmelody.docnap.core.published.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.netmelody.docnap.core.testsupport.matcher.ThePathToAPopulatedDirectory.thePathToAPopulatedDirectory;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.netmelody.docnap.core.testsupport.StateFactory;
import org.netmelody.docnap.core.testsupport.checker.DocnapCoreChecker;
import org.netmelody.docnap.core.testsupport.driver.DocnapCoreDriver;

public final class DocnapTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();
    
    private final StateFactory stateFactory = new StateFactory(folder);

    private DocnapCoreDriver docnap;
    
    @Test
    public void supportsCreatingANewDocnapStoreWithStorageLocation() {
        final String path = given().thePathToAnEmptyFolder();
        docnap = given().aNewDocNapStoreLocatedAt(path);
        
        then().theStore().isEmpty();
        assertThat(path, is(thePathToAPopulatedDirectory()));
    }
    
    @Test
    public void supportsReopeningAnExistingStore() {
        final String path = given().thePathToAnEmptyFolder();
        docnap = given().aNewDocNapStoreLocatedAt(path).containingOneDocument();
        
        when().theStoreIsReopened();
        
        then().theStore().hasOneDocument();
    }
    
    private StateFactory given() {
        return this.stateFactory;
    }

    private DocnapCoreDriver when() {
        return this.docnap;
    }
    
    private DocnapCoreChecker then() {
        return new DocnapCoreChecker(this.docnap);
    }
}
