package org.netmelody.docnap.swingclient.testsupport.driver;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import com.objogate.wl.gesture.Gestures;
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
   }

   public void enterDirectory(String path) {
       inputText(path);
   }
   
   public void chooseFile(String fileName) {
       inputText(fileName);
   }

   
   // TODO I'm sure don't need this but can't find default implementation
   private class FileClipboardOwner implements ClipboardOwner {

       @Override
       public void lostOwnership(Clipboard clipboard, Transferable contents) {
           // do nothing
       }
        
   }
}

