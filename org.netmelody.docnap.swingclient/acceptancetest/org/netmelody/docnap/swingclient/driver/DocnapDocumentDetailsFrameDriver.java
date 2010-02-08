package org.netmelody.docnap.swingclient.driver;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.ComponentDriver;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTextFieldDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class DocnapDocumentDetailsFrameDriver extends JFrameDriver {

    @SuppressWarnings("unchecked")
    public DocnapDocumentDetailsFrameDriver(DocnapApplicationDriver applicationDriver) {
        //TODO Should we user the gesture performer of the applicationDriver here?
        super(new GesturePerformer(), new AWTEventQueueProber(), named("documentWindow"), showingOnScreen());
    }

    public void andTitleIt(String title) {
        @SuppressWarnings("unchecked")
        final JTextFieldDriver titleFieldDriver = new JTextFieldDriver(this, JTextField.class, ComponentDriver.named("titleField"));
        titleFieldDriver.is(showingOnScreen());
        titleFieldDriver.replaceAllText(title);
        
        @SuppressWarnings("unchecked")
        final JButtonDriver saveButtonDriver = new JButtonDriver(this, JButton.class, ComponentDriver.named("saveBtn"));
        saveButtonDriver.click();
        dispose();
    }

    public void andSaveTheDocumentFileTo(String outFilename) {
        @SuppressWarnings("unchecked")
        final JButtonDriver retrieveFileDriver = new JButtonDriver(this, JButton.class, ComponentDriver.named("retrieveBtn"));
        retrieveFileDriver.click();
        
        new DocnapSaveFileChooserDriver(this).chooseFile(outFilename);
        dispose();
    }
    
    public void indexFileCalled(String filename) {
        @SuppressWarnings("unchecked")
        final JButtonDriver browseButtonDriver = new JButtonDriver(this, JButton.class, ComponentDriver.named("browseBtn"));
        browseButtonDriver.click();
        
        final DocnapIndexFileChooserDriver driver = new DocnapIndexFileChooserDriver(this);
        driver.chooseFile(filename);
    }
}
