package org.netmelody.docnap.swingclient.testsupport.driver;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.netmelody.docnap.swingclient.testsupport.probe.FileModifiedProbe;

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

    // TODO Make this find the window for a given document title
    public DocnapDocumentDetailsFrameDriver(DocnapApplicationDriver applicationDriver, String title) {
        this(applicationDriver);
        
        titleField().hasText(title);
    }
    
    private JTextFieldDriver titleField() {
        @SuppressWarnings("unchecked")
        final JTextFieldDriver titleFieldDriver = new JTextFieldDriver(this, JTextField.class, named("titleField"));
        titleFieldDriver.is(showingOnScreen());
        return titleFieldDriver;
    }

    public DocnapDocumentDetailsFrameDriver andTitleIt(String title) {
        titleField().replaceAllText(title);
        
        @SuppressWarnings("unchecked")
        final JButtonDriver saveButtonDriver = new JButtonDriver(this, JButton.class, named("saveBtn"));
        saveButtonDriver.click();
        return this;
    }

    public DocnapDocumentDetailsFrameDriver andSaveTheDocumentFileTo(String outFilename) {
        @SuppressWarnings("unchecked")
        final JButtonDriver retrieveFileDriver = new JButtonDriver(this, JButton.class, named("retrieveBtn"));
        retrieveFileDriver.click();
        
        final FileModifiedProbe fileModifiedProbe = new FileModifiedProbe(outFilename);
        new DocnapSaveFileChooserDriver(this).chooseFile(outFilename);
        
        check(fileModifiedProbe);
        
        return this;
    }
    
    public DocnapDocumentDetailsFrameDriver andTagIt(String tagTitle) {
        @SuppressWarnings("unchecked")
        final EditableComboDriver tagComboDriver = new EditableComboDriver(this, JComboBox.class, ComponentDriver.named("tagsComboBox"));
        tagComboDriver.is(showingOnScreen());
        tagComboDriver.replaceAllText(tagTitle);
        
        @SuppressWarnings("unchecked")
        final JButtonDriver addTagDriver = new JButtonDriver(this, JButton.class, named("addTagBtn"));
        addTagDriver.click();
        
        return this;
    }
    
    public void andCloseTheWindow() {
        dispose();
    }
    
    @SuppressWarnings("unchecked")
    public void showsTag(String tagTitle) {
        final JButtonDriver tagButtonDriver = new JButtonDriver(this, JButton.class, named(tagTitle + "Btn"));
        tagButtonDriver.is(showingOnScreen());
        
        final JButtonDriver tagRemoveButtonDriver = new JButtonDriver(this, JButton.class, named(tagTitle + "RemoveBtn"));
        tagRemoveButtonDriver.is(showingOnScreen());
    }
    
    public void indexFileCalled(String filename) {
        @SuppressWarnings("unchecked")
        final JButtonDriver browseButtonDriver = new JButtonDriver(this, JButton.class, ComponentDriver.named("browseBtn"));
        browseButtonDriver.click();
        
        final DocnapIndexFileChooserDriver driver = new DocnapIndexFileChooserDriver(this);
        driver.chooseFile(filename);
    }
}
