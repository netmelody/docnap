package org.netmelody.docnap.core.published.testsupport.checker;

import static org.netmelody.docnap.core.testsupport.utilities.AssertUtility.assertEqualsWithNull;

import java.io.IOException;

import org.netmelody.docnap.core.published.testsuport.domain.DocnapTag;
import org.netmelody.docnap.core.published.testsuport.domain.TestTag;

// TODO is this class the best way of doing this
// think this should be moved to test tag
public class DocnapTagChecker {

    public static void checkTagProperties(TestTag testTag, DocnapTag docnapTag) throws IOException{
        
        assertEqualsWithNull("Tag title not correct", testTag.getTitle(), docnapTag.getTag().getTitle());
    }
}
