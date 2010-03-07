package org.netmelody.docnap.swingclient.testsupport.driver;

import com.objogate.wl.swing.driver.JFileChooserDriver;
import com.objogate.wl.swing.driver.JFrameDriver;

public class DocnapIndexFileChooserDriver extends JFileChooserDriver {

    public DocnapIndexFileChooserDriver(JFrameDriver applicationDriver) {
        super(applicationDriver, named("indexFileChooser"));
    }
    
    public void chooseFile(String fileName) {
        enterManually(fileName);
        approve();
    }
}
