package org.netmelody.docnap.swingclient;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.swingclient.controls.BrowseBar;
import org.netmelody.docnap.swingclient.controls.DocumentContentPanel;
import org.netmelody.docnap.swingclient.controls.TagBar;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

public final class DocumentWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private final ApplicationContext applicationContext;
    private final IDocumentRepository documentRepository;
    private final ITagRepository tagRepository;
    
    private final PresentationModel<Document> documentPresentationModel = new PresentationModel<Document>(new Document());
    private final ValueModel fileModel = new ValueHolder();
    
    private final TagBar tagBar;
    private final BrowseBar browseBar;
    private final DocumentContentPanel documentViewer;
    

    public DocumentWindow(ApplicationContext applicationContext, IDocumentRepository documentRepository, ITagRepository tagRepository) {
        super();
        setName("documentWindow");
        
        this.applicationContext = applicationContext;
        this.documentRepository = documentRepository;
        this.tagRepository = tagRepository;
        
        this.tagBar = new TagBar(this.applicationContext, this.tagRepository);
        this.browseBar = new BrowseBar(this.applicationContext);
        this.browseBar.setDirectoryOnly(false);
        this.browseBar.connect(this.fileModel);
        this.documentViewer = new DocumentContentPanel(this.applicationContext, this.documentRepository);
        
        this.fileModel.addValueChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                documentChanged();
            }
        });
        
        initialiseComponents();
        documentChanged();
    }
    
    private void initialiseComponents() {
        // Document Identity
        final JLabel identityLabel = new JLabel();
        identityLabel.setName("identityLabel");
        final AbstractValueModel identityModel = this.documentPresentationModel.getModel(Document.PROPERTYNAME_IDENTITY);
        final JTextField identityField = BasicComponentFactory.createIntegerField(identityModel);
        identityField.setEditable(false);
        
        // Document Title
        final JLabel titleLabel = new JLabel();
        titleLabel.setName("titleLabel");
        final AbstractValueModel titleModel = this.documentPresentationModel.getModel(Document.PROPERTYNAME_TITLE);
        final JTextField titleField = BasicComponentFactory.createTextField(titleModel);
        
        // Date added
        final JLabel dateAddedLabel = new JLabel();
        dateAddedLabel.setName("dateAddedLabel");
        final AbstractValueModel dateAddedModel = this.documentPresentationModel.getModel(Document.PROPERTYNAME_DATEADDEDBASIC);
        final JTextField dateAddedField = BasicComponentFactory.createDateField(dateAddedModel);
        dateAddedField.setEditable(false);
        
        // Original File Name
        final JLabel fileNameLabel = new JLabel();
        fileNameLabel.setName("fileNameLabel");
        final AbstractValueModel fileNameModel = this.documentPresentationModel.getModel(Document.PROPERTYNAME_ORIGINALFILENAME);
        final JTextField fileNameField = BasicComponentFactory.createTextField(fileNameModel);
        fileNameField.setEditable(false);

        final DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("p, 2dlu, p:g"));
        builder.setDefaultDialogBorder();
        builder.append(identityLabel, identityField);
        builder.append(titleLabel, titleField);
        builder.append(dateAddedLabel, dateAddedField);
        builder.append(fileNameLabel, fileNameField);
        
        final JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.add(builder.getPanel(), BorderLayout.PAGE_START);
        innerPanel.add(this.documentViewer, BorderLayout.CENTER);
        
        add(this.browseBar, BorderLayout.PAGE_START);
        add(innerPanel, BorderLayout.CENTER);
        
        this.applicationContext.getResourceMap(DocumentWindow.class).injectComponents(this);
        final ApplicationActionMap actionMap = this.applicationContext.getActionMap(this);
        
        final JButton button = new JButton();
        button.setAction(actionMap.get("save"));
        final JButton button2 = new JButton();
        button2.setAction(actionMap.get("retrieve"));
        add(ButtonBarFactory.buildRightAlignedBar(button, button2), BorderLayout.PAGE_END);
    }

    public final void setDocument(Document document) {
        remove(this.browseBar);
        remove(this.tagBar);
        
        final Document newDocument = (null == document) ? new Document() : document;
        if (null != newDocument.getIdentity()) {
            this.tagBar.setDocumentId(newDocument.getIdentity());
            add(this.tagBar, BorderLayout.PAGE_START);
        }
        else {
            add(this.browseBar, BorderLayout.PAGE_START);
        }
        this.documentPresentationModel.setBean(newDocument);
        validate();
        documentChanged();
    }
    
    public final Document getDocument() {
        return this.documentPresentationModel.getBean();
    }
    
    protected void documentChanged() {
        final boolean validForSave = isValidForSave();
        firePropertyChange("validForSave", !validForSave, validForSave);
        
        final boolean validForRetrieve = isValidForRetrieve();
        firePropertyChange("validForRetrieve", !validForRetrieve, validForRetrieve);
        
        if (validForRetrieve) {
            this.documentViewer.setDocument(getDocument());
        }
        else {
            this.documentViewer.setFile(validForSave ? (File)this.fileModel.getValue() : null);
        }
    }

    public boolean isValidForSave() {
        if (null != getDocument() && null != getDocument().getIdentity()) {
            return true;
        }
        
        final Object file = this.fileModel.getValue();
        return (file instanceof File) && ((File)file).isFile();
    }

    @Action(enabledProperty="validForSave")
    public void save() {
        final Document currentDocument = getDocument();
        
        Document savedDocument = currentDocument;
        if (null == currentDocument.getIdentity()) {
            savedDocument = this.documentRepository.addFile(this.browseBar.getChosenFile());
            savedDocument.setTitle(currentDocument.getTitle());
        }
        
        setDocument(this.documentRepository.save(savedDocument));
    }
       
    public boolean isValidForRetrieve() {
        return null != getDocument().getIdentity();
    }

    @Action(enabledProperty="validForRetrieve")
    public void retrieve() {
        final String originalFilename = getDocument().getOriginalFilename();
        final String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        
        final JFileChooser fileChooser = new JFileChooser(); 
        fileChooser.setDialogTitle("");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setSelectedFile(new File(originalFilename));
        fileChooser.setFileFilter(new FileFilter() {
            
            @Override
            public String getDescription() {
                return "*"+extension;
            }
            
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(extension);
            }
        });

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { 
            final File file = fileChooser.getSelectedFile();
            this.documentRepository.retrieveFile(getDocument(), file);
        }
    }
}
