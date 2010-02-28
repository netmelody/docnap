package org.netmelody.docnap.swingclient.testsupport.driver;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JFileChooser;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.swing.ComponentManipulation;
import com.objogate.wl.swing.driver.JFileChooserDriver;
import com.objogate.wl.swing.driver.JFrameDriver;

public class DocnapFileChooserDriver extends JFileChooserDriver {
    
   private static final int TYPE_INPUT_TYPE = 1;
   private static final int PASTE_INPUT_TYPE = 2;
   
   private static final int inputType = PASTE_INPUT_TYPE; 

   public DocnapFileChooserDriver(JFrameDriver applicationDriver, String chooserName) {
        super(applicationDriver, named(chooserName));
        // TODO question why this makes it work
        this.is(showingOnScreen()); // This line makes it work
   }
   
   private void inputText(String text) {
       if (inputType == TYPE_INPUT_TYPE) {
           enterManually(text);
       }
       else if (inputType == PASTE_INPUT_TYPE){
           Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
           clipboard.setContents(new StringSelection(text), new FileClipboardOwner());
           performGesture(Gestures.paste());
       }
       approve();
       dispose();
   }

   public void enterDirectory(String path) {
       inputText(path);
   }
   
   public void chooseFile(String fileName) {
       inputText(fileName);
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

