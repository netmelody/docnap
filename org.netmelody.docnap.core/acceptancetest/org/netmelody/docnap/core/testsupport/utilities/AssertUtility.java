package org.netmelody.docnap.core.testsupport.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AssertUtility {
    
    public static void assertEqualsWithNull(String message, String expected, String actual) {
        if (null == expected) {
            assertNull(message, actual);
        }
        else {
            assertEquals(message, expected, actual);
        }
    }

}
