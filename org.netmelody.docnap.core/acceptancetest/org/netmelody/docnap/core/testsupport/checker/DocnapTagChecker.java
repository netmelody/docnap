package org.netmelody.docnap.core.testsupport.checker;

import static org.netmelody.docnap.core.testsupport.utilities.AssertUtility.assertEqualsWithNull;

import java.io.IOException;

import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.core.testsupport.domain.DocnapStoreTestGroup;
import org.netmelody.docnap.core.testsupport.domain.DocnapTag;
import org.netmelody.docnap.core.testsupport.domain.TestTag;

public class DocnapTagChecker implements IDocnapObjectChecker<TestTag, DocnapTag>{

    DocnapStoreTestGroup testStore;
    ITagRepository tagRepository;
    
    public DocnapTagChecker(DocnapStoreTestGroup testStore, ITagRepository tagRepository) {
        this.testStore = testStore;
        this.tagRepository = tagRepository;
    }
    
    public void equalsDocnapInstance(TestTag testTag, DocnapTag docnapTag) throws IOException{
        
        assertEqualsWithNull("Tag title not correct", testTag.getTitle(), docnapTag.getTag().getTitle());
    }
    
    public void hasCorrectLinks(TestTag testTag, DocnapTag docnapTag) {
        
    }
}
