package org.netmelody.docnap.swingclient.driver;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.netmelody.docnap.swingclient.probe.FileModifiedProbe;

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

    public DocnapDocumentDetailsFrameDriver andTitleIt(String title) {
        @SuppressWarnings("unchecked")
        final JTextFieldDriver titleFieldDriver = new JTextFieldDriver(this, JTextField.class, ComponentDriver.named("titleField"));
        titleFieldDriver.is(showingOnScreen());
        titleFieldDriver.replaceAllText(title);
        
        @SuppressWarnings("unchecked")
        final JButtonDriver saveButtonDriver = new JButtonDriver(this, JButton.class, ComponentDriver.named("saveBtn"));
        saveButtonDriver.click();
        dispose();
        return this;
    }

    public void andSaveTheDocumentFileTo(String outFilename) {
        @SuppressWarnings("unchecked")
        final JButtonDriver retrieveFileDriver = new JButtonDriver(this, JButton.class, ComponentDriver.named("retrieveBtn"));
        retrieveFileDriver.click();
        
        final FileModifiedProbe fileModifiedProbe = new FileModifiedProbe(outFilename);
        new DocnapSaveFileChooserDriver(this).chooseFile(outFilename);
        
        check(fileModifiedProbe);
        
        dispose();
    }
    
    public DocnapDocumentDetailsFrameDriver andTagTheDocumentWithTag(String tagTitle) {
        @SuppressWarnings("unchecked")
        final EditableComboDriver tagComboDriver = new EditableComboDriver(this, JComboBox.class, ComponentDriver.named("tagsComboBox"));
        tagComboDriver.is(showingOnScreen());
        tagComboDriver.replaceAllText(tagTitle);
        
        @SuppressWarnings("unchecked")
        final JButtonDriver addTagDriver = new JButtonDriver(this, JButton.class, ComponentDriver.named("addTagBtn"));
        addTagDriver.click();
        //dispose();
        
        return this;
    }
    
    public void andCloseWindow() {
        dispose();
    }
    
    public void andCheckTheTags(String[] tagTitles)  {
        
        for (String tagTitle : tagTitles) {
            @SuppressWarnings("unchecked") 
            final JButtonDriver tagButtonDriver = new JButtonDriver(this, JButton.class, ComponentDriver.named(tagTitle + "Btn"));
            tagButtonDriver.is(showingOnScreen());
            
            @SuppressWarnings("unchecked") 
            final JButtonDriver removeTagButtonDriver = new JButtonDriver(this, JButton.class, ComponentDriver.named(tagTitle + "RemoveBtn"));
            removeTagButtonDriver.is(showingOnScreen());
            
            //@SuppressWarnings("unchecked") 
            //final JComboBoxDriver tagComboDriver = new JComboBoxDriver(this, JComboBox.class, ComponentDriver.named("tagsComboBox"));
            //tagComboDriver.check(new Probe() {});
        }
    }
    
    public void indexFileCalled(String filename) {
        @SuppressWarnings("unchecked")
        final JButtonDriver browseButtonDriver = new JButtonDriver(this, JButton.class, ComponentDriver.named("browseBtn"));
        browseButtonDriver.click();
        
        final DocnapIndexFileChooserDriver driver = new DocnapIndexFileChooserDriver(this);
        driver.chooseFile(filename);
    }
}
