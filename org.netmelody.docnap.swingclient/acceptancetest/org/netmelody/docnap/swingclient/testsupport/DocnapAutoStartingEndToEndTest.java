package org.netmelody.docnap.swingclient.testsupport;

import org.junit.Before;

/**
 * Framework for end-to-end testing of the Docnap Swing user interface.
 * 
 * <p>Automatically starts the application in a clean directory as part of
 * set-up, and disposes it at tear-down.</p>
 * 
 * @author Tom Denley
 *
 */
public class DocnapAutoStartingEndToEndTest extends DocnapEndToEndTest {

    @Before
    public final void autoStartTheApplication() {
        startTheApplication();
    }
}
