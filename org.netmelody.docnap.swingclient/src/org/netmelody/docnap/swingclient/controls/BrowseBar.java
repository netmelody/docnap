package org.netmelody.docnap.swingclient.controls;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.xml.ws.Action;

import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

public class BrowseBar extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private final ValueModel fileNameModel = new ValueHolder();
    private boolean directoryOnly = false;

    public BrowseBar(ApplicationContext applicationContext) {
        super();
        
        final ApplicationActionMap actionMap = applicationContext.getActionMap(this);
        
        add(BasicComponentFactory.createTextField(this.fileNameModel), BorderLayout.CENTER);
        add(new JButton(actionMap.get("browse")), BorderLayout.LINE_END);
    }
    
    @Action
    public void browse() {
        final JFileChooser fileChooser = new JFileChooser(); 
        fileChooser.setDialogTitle("");
        fileChooser.setFileSelectionMode(this.directoryOnly ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(!this.directoryOnly);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
            final File file = fileChooser.getSelectedFile();
            try {
                this.fileNameModel.setValue(file.getCanonicalPath());
            }
            catch (IOException e) {
                this.fileNameModel.setValue(null);
            }
        }
    }
    
    public File getChosenFile() {
        final String fileName = (String)this.fileNameModel.getValue();
        if (null == fileName) {
            return null;
        }
        
        final File file = new File(fileName);
     
        if ((this.directoryOnly && file.isDirectory()) || (!this.directoryOnly && file.isFile())) {
            return file;
        }
        return null;
    }
}
