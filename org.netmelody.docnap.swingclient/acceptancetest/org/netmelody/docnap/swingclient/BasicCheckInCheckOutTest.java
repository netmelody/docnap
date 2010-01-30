package org.netmelody.docnap.swingclient;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasicCheckInCheckOutTest {
    private DocnapApplicationDriver applicationDriver;

    @Before
    public void runTheApplication() {
        DocnapMain.main(new String[0]);
        applicationDriver = new DocnapApplicationDriver();
    }

    @After
    public void stopTheApplication() {
        applicationDriver.dispose();
    }

    @Test
    public void testCheckingInADocumentAndGettingItOutAgain() {
        applicationDriver.clickTheIndexFileButtonOnTheToolBar();
    }
    
}
