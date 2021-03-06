package org.netmelody.docnap.swingclient;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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

public final class DocumentWindow extends DocnapWindow {

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
       this(applicationContext, documentRepository, tagRepository, null);
    }
    
    public DocumentWindow(ApplicationContext applicationContext, IDocumentRepository documentRepository, ITagRepository tagRepository, File startFile) {
        super("documentWindow");
                
        this.applicationContext = applicationContext;
        this.documentRepository = documentRepository;
        this.tagRepository = tagRepository;
        
        this.tagBar = new TagBar(this.applicationContext, this.tagRepository);
        this.tagBar.addDataChangedListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent ent) {
               fireDataChangedEvent();
            }
        });
        this.browseBar = new BrowseBar(this.applicationContext);
        this.browseBar.setDirectoryOnly(false);
        this.fileModel.setValue(startFile);
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
        
        // Original File Name
        final JLabel fileNameLabel = new JLabel();
        fileNameLabel.setName("fileNameLabel");
        final AbstractValueModel fileNameModel = this.documentPresentationModel.getModel(Document.PROPERTYNAME_ORIGINALFILENAME);
        final JTextField fileNameField = BasicComponentFactory.createTextField(fileNameModel);
        fileNameField.setEditable(false);
        
        // Document Title
        final JLabel titleLabel = new JLabel();
        titleLabel.setName("titleLabel");
        final AbstractValueModel titleModel = this.documentPresentationModel.getModel(Document.PROPERTYNAME_TITLE);
        final JTextField titleField = BasicComponentFactory.createTextField(titleModel);
        titleField.setName("titleField");
        
        // Date sent
        final JLabel dateSentLabel = new JLabel();
        dateSentLabel.setName("dateSentLabel");
        final AbstractValueModel dateSentModel = new DocnapDateTimeConverter(this.documentPresentationModel.getModel(Document.PROPERTYNAME_DATESENT));
        final JTextField dateSentField = BasicComponentFactory.createDateField(dateSentModel);
        
        // Date received
        final JLabel dateReceivedLabel = new JLabel();
        dateReceivedLabel.setName("dateReceivedLabel");
        final AbstractValueModel dateReceivedModel = new DocnapDateTimeConverter(this.documentPresentationModel.getModel(Document.PROPERTYNAME_DATERECEIVED));
        final JTextField dateReceivedField = BasicComponentFactory.createDateField(dateReceivedModel);
        
        // Date added
        final JLabel dateAddedLabel = new JLabel();
        dateAddedLabel.setName("dateAddedLabel");
        final AbstractValueModel dateAddedModel = new DocnapDateTimeConverter(documentPresentationModel.getModel(Document.PROPERTYNAME_DATEADDED));
        final JTextField dateAddedField = BasicComponentFactory.createDateField(dateAddedModel);
        dateAddedField.setEditable(false);

        final DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("p, 2dlu, p:g"));
        builder.setDefaultDialogBorder();
        builder.append(identityLabel, identityField);
        builder.append(fileNameLabel, fileNameField);
        builder.append(titleLabel, titleField);
        builder.append(dateSentLabel, dateSentField);
        builder.append(dateReceivedLabel, dateReceivedField);
        builder.append(dateAddedLabel, dateAddedField);
        
        final JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.add(builder.getPanel(), BorderLayout.PAGE_START);
        innerPanel.add(this.documentViewer, BorderLayout.CENTER);
        
        add(this.browseBar, BorderLayout.PAGE_START);
        add(innerPanel, BorderLayout.CENTER);
        
        this.applicationContext.getResourceMap(DocumentWindow.class).injectComponents(this);
        final ApplicationActionMap actionMap = this.applicationContext.getActionMap(this);
        
        final JButton button = new JButton(actionMap.get("save"));
        button.setName("saveBtn");
        final JButton button2 = new JButton(actionMap.get("retrieve"));
        button2.setName("retrieveBtn");
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
            savedDocument.setDateSent(currentDocument.getDateSent());
            savedDocument.setDateReceived(currentDocument.getDateReceived());
        }
        
        setDocument(this.documentRepository.save(savedDocument));
        fireDataChangedEvent();
    }
       
    public boolean isValidForRetrieve() {
        return null != getDocument().getIdentity();
    }

    @Action(enabledProperty="validForRetrieve")
    public void retrieve() {
        final String originalFilename = getDocument().getOriginalFilename();
        final String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setName("saveFileChooser");
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

        //TODO: Work around for WindowLicker bug - Should be fileChooser.showSaveDialog
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setApproveButtonText("Save");
        if (fileChooser.showDialog(this, null) == JFileChooser.APPROVE_OPTION) { 
            final File file = fileChooser.getSelectedFile();
            this.documentRepository.retrieveFile(getDocument(), file);
        }
    }


 
    
    
}
