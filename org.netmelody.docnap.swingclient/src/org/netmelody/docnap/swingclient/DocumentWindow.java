package org.netmelody.docnap.swingclient;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.published.IDocumentRepository;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.AbstractValueModel;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class DocumentWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private final ApplicationContext applicationContext;
    private final IDocumentRepository documentRepository;
    
    private final PresentationModel<Document> documentPresentationModel = new PresentationModel<Document>(new Document());

    public DocumentWindow(ApplicationContext applicationContext, IDocumentRepository documentRepository) {
        super();
        setName("documentWindow");
        
        this.applicationContext = applicationContext;
        this.documentRepository = documentRepository;
        initialiseComponents();
    }
    
    private void initialiseComponents() {
        final JLabel titleLabel = new JLabel();
        titleLabel.setName("titleLabel");
        
        final AbstractValueModel titleModel = this.documentPresentationModel.getModel(Document.PROPERTYNAME_TITLE);
        final JTextField titleField = BasicComponentFactory.createTextField(titleModel);
        final DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout("p, 2dlu, p:g"));

        builder.setDefaultDialogBorder();
        builder.append(titleLabel, titleField);
        add(builder.getPanel(), BorderLayout.CENTER);
        
        this.applicationContext.getResourceMap(DocumentWindow.class).injectComponents(this);
        final ApplicationActionMap actionMap = this.applicationContext.getActionMap(this);
        final JButton button = new JButton();
        button.setAction(actionMap.get("save"));

        add(button, BorderLayout.SOUTH);
    }
    
    public final void setDocument(Document document) {
        this.documentPresentationModel.setBean(document);
    }
    
    public final Document getDocument() {
        return this.documentPresentationModel.getBean();
    }
    
    @Action
    public void save() {
        final Document savedDocument = this.documentRepository.save(getDocument());
        setDocument(savedDocument);
    }
}
