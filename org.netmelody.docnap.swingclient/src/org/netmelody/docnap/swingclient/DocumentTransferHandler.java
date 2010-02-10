package org.netmelody.docnap.swingclient;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Collection;

import javax.swing.JList;
import javax.swing.TransferHandler;

import org.jdesktop.application.ApplicationContext;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;

public class DocumentTransferHandler extends TransferHandler {
    private static final long serialVersionUID = 1L;
    
    private final DocnapApplication application;
    private final ApplicationContext context;
    private final IDocumentRepository documentRepository;
    private final ITagRepository tagRepository;
    
    public DocumentTransferHandler(DocnapApplication application, IDocumentRepository docRepository, ITagRepository tagRepository) {
        this.application = application;
        this.context = application.getContext();
        this.documentRepository = docRepository;
        this.tagRepository = tagRepository;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#canImport(javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean canImport(TransferSupport support) {
        if (support.getComponent() instanceof JList) {
            for (DataFlavor dataFlavor : support.getDataFlavors()) {
                if (!dataFlavor.equals(DataFlavor.javaFileListFlavor))
                    return false;
            }
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#importData(javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        
        Transferable transfer = support.getTransferable();
        Object transferedData = null;
        try {
            transferedData = transfer.getTransferData(DataFlavor.javaFileListFlavor);
        }
        catch (Exception exception)
        {
            System.out.println("Failed to get transfered data from drag and drop");
            exception.printStackTrace();
            return false;
            
        }
 
        Collection<?> fileList = (Collection<?>)transferedData;
        
        for (Object file : fileList) {
            DocumentWindow fileWindow = new DocumentWindow(context, documentRepository, tagRepository, (File)file);
            application.show(fileWindow); 
        }
        
        return true;
    }
}
