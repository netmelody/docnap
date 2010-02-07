package org.netmelody.docnap.swingclient;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.Collection;

import javax.swing.JList;
import javax.swing.TransferHandler;

import org.netmelody.docnap.core.published.IDocumentRepository;

public class DocumentTransferHandler extends TransferHandler {
    private static final long serialVersionUID = 1L;
    
    private final IDocumentRepository documentRepository;
    
    public DocumentTransferHandler(IDocumentRepository docRepository) {
        this.documentRepository = docRepository;
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
            //
            return false;
            
        }
 
        Collection<File> fileList = (Collection<File>)transferedData;
        
        for (File file : fileList) {
            documentRepository.addFile(file);
            
        }
        
        return true;
     
    }
    
    
}
