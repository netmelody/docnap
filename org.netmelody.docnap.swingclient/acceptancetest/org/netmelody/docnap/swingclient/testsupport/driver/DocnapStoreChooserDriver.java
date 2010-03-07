package org.netmelody.docnap.swingclient.testsupport.driver;

import com.objogate.wl.swing.driver.JFileChooserDriver;

public class DocnapStoreChooserDriver extends JFileChooserDriver {

    public DocnapStoreChooserDriver(DocnapApplicationDriver applicationDriver) {
        super(applicationDriver, named("homeChooser"));
    }
    
    public void enterDirectory(String path) {
        enterManually(path);
        approve();
    }
}
