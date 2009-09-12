package org.netmelody.docnap.swingclient.controls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jdesktop.application.ApplicationContext;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.exception.DocnapRuntimeException;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.swingclient.DocumentWindow;

public final class DocumentContentPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private final IDocumentRepository documentRepository;

    private final JLabel imageLabel;
    
    private File documentFile;

    public DocumentContentPanel(ApplicationContext applicationContext, IDocumentRepository documentRepository) {
        super(new BorderLayout());
        this.documentRepository = documentRepository;
        
        setVisible(false);
        
        //final ApplicationActionMap actionMap = applicationContext.getActionMap(this);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLineBorder(Color.BLACK)));
        this.imageLabel = new JLabel();
        this.imageLabel.setName("imageLabel");
        add(new JScrollPane(this.imageLabel), BorderLayout.CENTER);
        
        applicationContext.getResourceMap(DocumentWindow.class).injectComponents(this);
    }
    
    public void setDocument(Document document) {
        try {
            final File tempFile = File.createTempFile("doc", null);
            this.documentRepository.retrieveDocument(document, tempFile);
            setFile(tempFile);
        }
        catch (final IOException exception) {
            throw new DocnapRuntimeException("Invalid document", exception);
        }
    }
    
    public void setFile(File file) {
        this.documentFile = file;
        if (null == file) {
            setVisible(false);
            return;
        }
        updateDisplay();
    }
    
    private void updateDisplay() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(this.documentFile);
        }
        catch (IOException e) {
            // Just fall-through and display nothing.
        }
        
        if (null != image) {
            this.imageLabel.setIcon(new ImageIcon(image));
            setVisible(true);
        }
        else {
            setVisible(false);
        }
        invalidate();
    }
}
