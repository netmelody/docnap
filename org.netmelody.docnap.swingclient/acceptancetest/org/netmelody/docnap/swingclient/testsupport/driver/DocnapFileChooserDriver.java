package org.netmelody.docnap.swingclient.testsupport.driver;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JFileChooser;

import org.hamcrest.Matcher;

import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.swing.ComponentManipulation;
import com.objogate.wl.swing.driver.JFileChooserDriver;
import com.objogate.wl.swing.driver.JFrameDriver;

public class DocnapFileChooserDriver extends JFileChooserDriver {
    
   
   public enum EInputType {
       TYPE, PASTE, CLICK
   }
   
   private final EInputType inputType; 

   public DocnapFileChooserDriver(JFrameDriver applicationDriver, Matcher<? super JFileChooser> matcher) {
       this(applicationDriver, matcher, EInputType.TYPE);
   }
   
   public DocnapFileChooserDriver(JFrameDriver applicationDriver, Matcher<? super JFileChooser> matcher, EInputType inputType) {
        super(applicationDriver, matcher);
        this.inputType = inputType;
   }
   
   @Override
   public void enterManually(String text) {
       switch (inputType) {
           case PASTE: 
               Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
               clipboard.setContents(new StringSelection(text), new FileClipboardOwner());
               performGesture(Gestures.paste());
               break;
           case CLICK:
               
           case TYPE:
           default:
               super.enterManually(text);
       }
       
       approve();
      // dispose();
   }

   /**
    * Disposes of the frame and also sets the frame's name to <code>null</code> so that it cannot be found
    * by the {@link com.objogate.wl.swing.driver.ComponentDriver#named(String)} named} matcher in a subsequent
    * test while it is being garbage collected.
    */
   public void dispose() {
       //perform(description, manipulation)
       perform("disposing", new ComponentManipulation<JFileChooser>() {
           public void manipulate(JFileChooser component) {
               //component.
               component.setName(null);
           }
       });
   }
   
   
   // TODO I'm sure don't need this but can't find default implementation
   private class FileClipboardOwner implements ClipboardOwner {

       @Override
       public void lostOwnership(Clipboard clipboard, Transferable contents) {
           // do nothing
       }
        
   }
}

