package org.netmelody.docnap.swingclient.controls;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public final class BrowseBar extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private final ValueModel fileNameModel = new ValueHolder();
    private boolean directoryOnly = false;
    
    private ValueModel connectedModel;
    
    private final PropertyChangeListener connectedModelValueChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(final PropertyChangeEvent event) {
            String value = convertToString(event.getNewValue());
            if (null == value) {
                return;
            }
            BrowseBar.this.fileNameModel.setValue(value);
        }
    };

    private final PropertyChangeListener fileNameModelValueChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(final PropertyChangeEvent event) {
            if (null == connectedModel) {
                return;
            }
            BrowseBar.this.connectedModel.setValue(convertToFile(event.getNewValue()));
        }
    };
    
    public BrowseBar(ApplicationContext applicationContext) {
        super(new BorderLayout());
        final ApplicationActionMap actionMap = applicationContext.getActionMap(this);
        
        final DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("p:g, 2dlu, p"));
        builder.setDefaultDialogBorder();
        builder.append(BasicComponentFactory.createTextField(this.fileNameModel, false), new JButton(actionMap.get("browse")));
        add(builder.getPanel(), BorderLayout.CENTER);

        this.fileNameModel.addValueChangeListener(this.fileNameModelValueChangeListener);
    }
    
    @Action
    public void browse() {
        final JFileChooser fileChooser = new JFileChooser(); 
        fileChooser.setDialogTitle("");
        fileChooser.setFileSelectionMode(this.isDirectoryOnly() ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(!this.isDirectoryOnly());

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
        return convertToFile(this.fileNameModel.getValue());
    }

    protected File convertToFile(Object value) {
        final String fileName = (String)value;
        if (null == fileName) {
            return null;
        }
        
        final File file = new File(fileName);
     
        if ((this.isDirectoryOnly() && file.isDirectory()) || (!this.isDirectoryOnly() && file.isFile())) {
            return file;
        }
        return null;
    }
    
    protected String convertToString(Object value) {
        String result = null;
        if (value instanceof File) {
            try {
                result = ((File)value).getCanonicalPath();
            }
            catch (IOException e) {
                result = null;
            }
        }
        return result;
    }

    public void setDirectoryOnly(boolean directoryOnly) {
        this.directoryOnly = directoryOnly;
    }

    public boolean isDirectoryOnly() {
        return directoryOnly;
    }
    
    public void connect(ValueModel model) {
        disconnect();
        this.connectedModel = model;
        
        if (null == this.connectedModel) {
            return;
        }
        this.connectedModel.addValueChangeListener(this.connectedModelValueChangeListener);
        this.fileNameModel.setValue(convertToString(model.getValue()));
    }
    
    public void disconnect() {
        if (this.connectedModel != null) {
            this.connectedModel.removeValueChangeListener(this.connectedModelValueChangeListener);
            this.connectedModel = null;
        }
    }
}
