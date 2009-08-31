package org.netmelody.docnap.swingclient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.Bootstrap;
import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.picocontainer.PicoContainer;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;

public class DocnapApplication extends SingleFrameApplication {

    private static final String SETTINGS_FILE = "lasthome.xml";

    private static final String PROPERTYNAME_DOCUMENTSELECTED = "documentSelected";

    private static final Tag ALL_TAG = new Tag(null);
    static { ALL_TAG.setTitle("All"); };

    private Bootstrap bootstrap;
    
    private IDocnapStore docnapStore;
    private IDocumentRepository documentRepository;
    private ITagRepository tagRepository;

    private final SelectionInList<Tag> tagsModel = new SelectionInList<Tag>();
    private final SelectionInList<Document> documentsModel = new SelectionInList<Document>();

    private javax.swing.Action getAction(String actionName) {
        return getContext().getActionMap().get(actionName);
    }
    
    @Action
    public void chooseHomeDirectory() {
        final JFileChooser dirChooser = new JFileChooser(); 
        dirChooser.setDialogTitle("");
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setAcceptAllFileFilterUsed(false);

        if (dirChooser.showOpenDialog(getMainFrame()) == JFileChooser.APPROVE_OPTION) { 
            final File file = dirChooser.getSelectedFile();
            
            try {
                final String path = file.getCanonicalPath();
                this.docnapStore.setStorageLocation(path);
                getContext().getLocalStorage().save(path, SETTINGS_FILE);
            }
            catch (IOException exception) {
                throw new IllegalStateException("Failed to recognise chosen path [" + file + "].", exception);
            }
        }
    }
    
    @Action
    public void indexFile() {
        final JFileChooser fileChooser = new JFileChooser(); 
        fileChooser.setDialogTitle("");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(true);

        if (fileChooser.showOpenDialog(getMainFrame()) == JFileChooser.APPROVE_OPTION) { 
            final File file = fileChooser.getSelectedFile();
            this.documentRepository.addDocument(file);
        }
    }
    
    @Action(enabledProperty=DocnapApplication.PROPERTYNAME_DOCUMENTSELECTED)
    public void showDocument() {
        final DocumentWindow documentWindow = new DocumentWindow(getContext(), this.documentRepository, this.tagRepository);
        documentWindow.setDocument((Document)this.documentsModel.getSelection());
        show(documentWindow);
    }
    
    public boolean isDocumentSelected() {
        return !this.documentsModel.isSelectionEmpty();
    }
    
    private JMenu createMenu(String menuName, String[] actionNames) {
        JMenu menu = new JMenu();
        menu.setName(menuName);
        for (String actionName : actionNames) {
            if (actionName.equals("---")) {
                menu.add(new JSeparator());
            }
            else {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setAction(getAction(actionName));
                menuItem.setIcon(null);
                menu.add(menuItem);
            }
        }
        return menu;
    }

    private void updateTagList() {
        final List<Tag> tagList = new ArrayList<Tag>(this.tagRepository.fetchAll());
        tagList.add(0, ALL_TAG);
        this.tagsModel.setList(tagList);
    }
    
    private void updateDocumnentList() {
        if (this.tagsModel.isSelectionEmpty()) {
            this.documentsModel.setList(new ArrayList<Document>());
            return;
        }
        
        final Tag selectedTag = this.tagsModel.getSelection();
        if (ALL_TAG == selectedTag) {
            this.documentsModel.setList(new ArrayList<Document>(documentRepository.fetchAll()));
            return;
        }
        
        final Collection<Document> documents = this.documentRepository.findByTagId(selectedTag.getIdentity());
        this.documentsModel.setList(new ArrayList<Document>(documents));
    }
    
    private void addListModelListeners() {
        this.tagsModel.addPropertyChangeListener(SelectionInList.PROPERTYNAME_SELECTION, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateDocumnentList();
            }
        });
        
        this.documentsModel.addPropertyChangeListener(SelectionInList.PROPERTYNAME_SELECTION, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                final boolean newValue = isDocumentSelected();
                firePropertyChange(PROPERTYNAME_DOCUMENTSELECTED, !newValue, newValue);                
            }
        });
    }
    
    private JComponent createToolBar() {
        String[] toolbarActionNames = {
                "indexFile", "showDocument",
        };
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        Border border = new EmptyBorder(2, 9, 2, 9); // top, left, bottom, right
        for (String actionName : toolbarActionNames) {
            JButton button = new JButton();
            button.setBorder(border);
            button.setVerticalTextPosition(JButton.BOTTOM);
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setAction(getAction(actionName));
            button.setFocusable(false);
            toolBar.add(button);
        }
        return toolBar;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        String[] fileMenuActionNames = {"chooseHomeDirectory"};
        menuBar.add(createMenu("fileMenu", fileMenuActionNames));
        return menuBar;
    }

    private JComponent createMainPanel() {
        addListModelListeners();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createToolBar(), BorderLayout.NORTH);
        JList tagList = new JList();
        updateTagList();
        Bindings.bind(tagList, this.tagsModel);
        panel.add(tagList, BorderLayout.WEST);
        

        final JList documentList = new JList();
        Bindings.bind(documentList, this.documentsModel);
        final javax.swing.Action showDocumentAction = getAction("showDocument");
        documentList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (showDocumentAction.isEnabled()) {
                        showDocumentAction.actionPerformed(new ActionEvent(event.getSource(), ActionEvent.ACTION_PERFORMED, null));
                    }
                }
            }
        });
        panel.add(documentList, BorderLayout.CENTER);
        
        panel.setBorder(new EmptyBorder(0, 2, 2, 2)); // top, left, bottom, right
        panel.setPreferredSize(new Dimension(640, 480));
        return panel;
    }
    
    @Override
    protected void startup() {
        this.bootstrap = new Bootstrap();  
        PicoContainer appContext = this.bootstrap.start();
        
        getMainFrame().setJMenuBar(createMenuBar());
        
        //TODO: Antipattern - container dependency. Need to revise pico lifecycle of Swing app.
        this.docnapStore = appContext.getComponent(IDocnapStore.class);
        this.documentRepository = appContext.getComponent(IDocumentRepository.class);
        this.tagRepository = appContext.getComponent(ITagRepository.class);
        
        restoreHomePath();
        
        //final JLabel label = new JLabel(this.docnapStore.getStorageLocation());
        
        show(createMainPanel());
    }

    @Override
    protected void shutdown() {
        this.bootstrap.stop();
        super.shutdown();
    }
    
    /**
     * Restore the previously chosen home path.
     * 
     * @return
     *     
     */
    private void restoreHomePath() {
        final String homePath = getPreviousHomePath();
        if (0 == homePath.length()) {
            chooseHomeDirectory();
        }
        else {
            try {
                this.docnapStore.setStorageLocation(homePath);
            }
            catch (Exception exception) {
                System.out.println("Failed to open previous home of " + homePath + " (" + exception.getMessage() + ")");
                exception.printStackTrace();
                chooseHomeDirectory();
            }
        }
    }

    /**
     * Get the previously selected home directory from local storage. If no
     * previous home is found then return an empty string.
     * 
     * @return
     *     last used home path
     */
    private String getPreviousHomePath() {
        try {
            final String path = (String)getContext().getLocalStorage().load(SETTINGS_FILE);
            return (path == null) ? "" : path;
        }
        catch (IOException e) {
            return "";
        }
    }

}
