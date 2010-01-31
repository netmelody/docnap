package org.netmelody.docnap.swingclient;

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
