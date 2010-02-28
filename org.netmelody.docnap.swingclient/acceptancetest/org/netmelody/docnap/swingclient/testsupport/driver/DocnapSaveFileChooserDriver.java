package org.netmelody.docnap.swingclient.testsupport.driver;

import com.objogate.wl.swing.driver.JFrameDriver;

public class DocnapSaveFileChooserDriver extends DocnapFileChooserDriver {

    public DocnapSaveFileChooserDriver(JFrameDriver applicationDriver) {
        super(applicationDriver, "saveFileChooser");
    }
}
