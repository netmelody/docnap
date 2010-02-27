package org.netmelody.docnap.swingclient.testsupport;

import org.junit.Before;

public class DocnapAutoStartingEndToEndTest extends DocnapEndToEndTest {

    @Before
    public final void autoStartTheApplication() {
        startTheApplication();
    }
}
