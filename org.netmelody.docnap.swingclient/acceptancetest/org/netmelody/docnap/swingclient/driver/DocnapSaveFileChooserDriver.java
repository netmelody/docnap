package org.netmelody.docnap.swingclient.driver;

import com.objogate.wl.swing.driver.JFileChooserDriver;
import com.objogate.wl.swing.driver.JFrameDriver;

public class DocnapSaveFileChooserDriver extends JFileChooserDriver {

    public DocnapSaveFileChooserDriver(JFrameDriver applicationDriver) {
        super(applicationDriver, named("saveFileChooser"));
    }
    
    public void chooseFile(String fileName) {
        enterManually(fileName);
        approve();
    }
}
