package org.netmelody.docnap.swingclient.testsupport.driver;

import com.objogate.wl.swing.driver.JFrameDriver;

public class DocnapIndexFileChooserDriver extends DocnapFileChooserDriver {

    public DocnapIndexFileChooserDriver(JFrameDriver applicationDriver) {
        super(applicationDriver, "indexFileChooser");
    }
}
