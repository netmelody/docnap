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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.netmelody.docnap.core.published.ITagRepository;
import org.netmelody.docnap.swingclient.controls.TagBar;
import org.picocontainer.PicoContainer;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class DocnapApplication extends SingleFrameApplication {

    private static final String SETTINGS_FILE = "lasthome.xml";

    private static final String PROPERTYNAME_DOCUMENTSELECTED = "documentSelected";

    private static final Tag ALL_TAG = new Tag(null);
    static { ALL_TAG.setTitle("All"); };
    private static final int ALL_TAG_POSITION = 0;

    private Bootstrap bootstrap;
    
    private IDocnapStore docnapStore;
    private IDocumentRepository documentRepository;
    private ITagRepository tagRepository;
    
    private TagBar tagBar;

    private final SelectionInList<TagListEntry> tagsModel = new SelectionInList<TagListEntry>();
    private final SelectionInList<Document> documentsModel = new SelectionInList<Document>();

    private javax.swing.Action getAction(String actionName) {
        return getContext().getActionMap().get(actionName);
    }
    
    @Action
    public void chooseHomeDirectory() {
        final JFileChooser dirChooser = new JFileChooser();
        dirChooser.setName("homeChooser");
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
            updateTitle();
        }
    }
    
    @Action
    public void saveToZipFile() {
        final JFileChooser zipSaveChooser = new JFileChooser();
        final FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Zip files", "zip");
        zipSaveChooser.setFileFilter(extensionFilter);
        zipSaveChooser.setName("zipChooser");
        zipSaveChooser.setDialogTitle("");
        zipSaveChooser.setAcceptAllFileFilterUsed(false);
        
        /* TODO add zip extension if not typed */

        if (zipSaveChooser.showSaveDialog(getMainFrame()) == JFileChooser.APPROVE_OPTION) { 
            final File file = zipSaveChooser.getSelectedFile();
            this.documentRepository.retrieveAllFilesAsZip(file);
        }
    }
    
    private void updateTitle() {
        final String titleSplit = " - ";
        final String currentTitle = getMainFrame().getTitle();
        
        int splitIndex = currentTitle.indexOf(titleSplit);
        if (splitIndex < 0) {
            splitIndex = currentTitle.length();
        }
        
        final String newTitle = currentTitle.substring(0, splitIndex) +
                                titleSplit + this.docnapStore.getStorageLocation();
        getMainFrame().setTitle(newTitle);
    }

    @Action
    public void indexFile() {
        final DocumentWindow documentWindow = new DocumentWindow(getContext(), this.documentRepository, this.tagRepository);
        showDocnapWindow(documentWindow);
    }
    
    @Action(enabledProperty=DocnapApplication.PROPERTYNAME_DOCUMENTSELECTED)
    public void showDocument() {
        final DocumentWindow documentWindow = new DocumentWindow(getContext(), this.documentRepository, this.tagRepository);
        documentWindow.setDocument(this.documentsModel.getSelection());
        showDocnapWindow(documentWindow);
    }
    
    private void showDocnapWindow(DocnapWindow docnapWindow) {
        docnapWindow.addDataChangedListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateMainPanel();
            }
        });
        show(docnapWindow);
    }
    
    private void updateMainPanel() {
        updateTagList();
        updateDocumentList();
    }
    
    @Action(enabledProperty=DocnapApplication.PROPERTYNAME_DOCUMENTSELECTED)
    public void removeDocument() {
        Document documentToRemove = this.documentsModel.getSelection();
        
        if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "Remove document \"" + documentToRemove.toString() + "\"?", "Remove Document?", JOptionPane.YES_NO_OPTION)) {
            this.documentRepository.removeDocument(documentToRemove);
            updateMainPanel();
        }
    }
    
    @Action
    public void manageTags() {
        showDocnapWindow(new ManageTagsWindow(new ManageTagsModel(this.tagRepository)));
    }
    
    public boolean isDocumentSelected() {
        return !this.documentsModel.isSelectionEmpty();
    }
    
    private void addMenuItems(JComponent menu, String[] actionNames) {
        for (String actionName : actionNames) {
            if (actionName.equals("---")) {
                menu.add(new JSeparator());
            }
            else {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setName(actionName+"MenuItem");
                menuItem.setAction(getAction(actionName));
                menuItem.setIcon(null);
                menu.add(menuItem);
            }
        }
    }
    
    private JMenu createMenu(String menuName, String[] actionNames) {
        JMenu menu = new JMenu();
        menu.setName(menuName);
        addMenuItems(menu, actionNames);
        return menu;
    }
    
    private JPopupMenu createPopupMenu(String [] actionNames) {
        JPopupMenu menu = new JPopupMenu();
        addMenuItems(menu, actionNames);
        
        return menu;
    }
    
    private List<TagListEntry> convertTagListToTagListEntryList(List<Tag> tagList) {
        List<TagListEntry> tagListEntryList = new ArrayList<TagListEntry>();
        for (Tag tag : tagList) {
            tagListEntryList.add(new TagListEntry(tag));
        }
        return tagListEntryList;
    }

    private void updateTagList() {
        final List<Tag> tagList = new ArrayList<Tag>(this.tagRepository.fetchAll());
        tagList.add(ALL_TAG_POSITION, ALL_TAG);
        ALL_TAG.setDocumentCount(this.documentRepository.getNumberOfDocuments());
        this.tagsModel.setList(convertTagListToTagListEntryList(tagList));
    }
    
    private void updateDocumentList() {
        if (this.tagsModel.isSelectionEmpty()) {
            this.documentsModel.setList(new ArrayList<Document>());
            return;
        }
        
        final Tag selectedTag = this.tagsModel.getSelection().getTag();
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
                updateDocumentList();
            }
        });
        
        this.documentsModel.addPropertyChangeListener(SelectionInList.PROPERTYNAME_SELECTION, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                final boolean newValue = isDocumentSelected();
                firePropertyChange(PROPERTYNAME_DOCUMENTSELECTED, !newValue, newValue);   
                if (newValue) {
                  tagBar.setDocumentId(documentsModel.getSelection().getIdentity());
                }
                else {
                    tagBar.setDocumentId(null);
                }  
            }
        });
    }
    
    private JComponent createToolBar() {
        String[] toolbarActionNames = {
                "indexFile", "showDocument", "removeDocument", "manageTags"
        };
        JToolBar toolBar = new JToolBar("toolBar");
        toolBar.setFloatable(false);
        Border border = new EmptyBorder(2, 9, 2, 9); // top, left, bottom, right
        for (String actionName : toolbarActionNames) {
            JButton button = new JButton();
            button.setName(actionName);
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
        final JMenuBar menuBar = new JMenuBar();
        final String[] fileMenuActionNames = {"chooseHomeDirectory", "saveToZipFile", "---", "quit"};
        menuBar.add(createMenu("fileMenu", fileMenuActionNames));
        return menuBar;
    }

    private JComponent createMainPanel() {
        addListModelListeners();
        final JPanel mainPanel = new JPanel(new BorderLayout());
               
        mainPanel.add(createToolBar(), BorderLayout.PAGE_START);
      
        final JList tagList = new JList();
        tagList.setName("tagList");
        updateTagList();
        Bindings.bind(tagList, this.tagsModel);
        tagList.setSelectedIndex(ALL_TAG_POSITION);
        final JPanel tagPanel = new JPanel(new FormLayout("p:g", "p:g"));
        tagPanel.add(new JScrollPane(tagList), new CellConstraints(1,1,CellConstraints.FILL, CellConstraints.FILL));
        tagPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        mainPanel.add(tagPanel, BorderLayout.WEST);
        
        final JList documentList = new JList();
        documentList.setName("documentList");
        Bindings.bind(documentList, this.documentsModel);
        documentList.setTransferHandler(new DocumentTransferHandler(this, this.documentRepository, this.tagRepository));
        final javax.swing.Action showDocumentAction = getAction("showDocument");
        documentList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (showDocumentAction.isEnabled()) {
                        showDocumentAction.actionPerformed(new ActionEvent(event.getSource(), ActionEvent.ACTION_PERFORMED, null));
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent event) {
                if (event.isPopupTrigger()) {
                    JPopupMenu popup = createPopupMenu(new String[] {"showDocument", "removeDocument"});
                    popup.show(documentList, event.getX(), event.getY());
                }
            }
            @Override
            public void mouseReleased(MouseEvent event) {
                if (event.isPopupTrigger()) {
                    JPopupMenu popup = createPopupMenu(new String[] {"showDocument", "removeDocument"});
                    popup.show(documentList, event.getX(), event.getY());
                }
            }
        });
        final JPanel documentPanel = new JPanel(new FormLayout("p:g", "top:pref, p:grow"));
        tagBar = new TagBar(this.getContext(), this.tagRepository);
        documentPanel.add(tagBar, new CellConstraints(1,1));
        documentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        tagBar.setDocumentId(null);
        
        final JPanel documentListPanel = new JPanel(new FormLayout("p:g", "p:g"));
        documentPanel.add(documentListPanel, new CellConstraints(1, 2, CellConstraints.FILL, CellConstraints.FILL));
        
        documentListPanel.add(new JScrollPane(documentList), new CellConstraints(1,1,CellConstraints.FILL, CellConstraints.FILL));
        documentListPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        mainPanel.add(documentPanel, BorderLayout.CENTER);
        
        mainPanel.setBorder(new EmptyBorder(0, 2, 2, 2)); // top, left, bottom, right
        mainPanel.setPreferredSize(new Dimension(640, 480));
        return mainPanel;
    }
    
    @Override
    protected void initialize(String[] args) {
        this.bootstrap = new Bootstrap();
        PicoContainer container = this.bootstrap.start(getContext());
        
        //TODO: Antipattern - container dependency. Need to revise pico lifecycle of Swing app.
        this.docnapStore = container.getComponent(IDocnapStore.class);
        this.documentRepository = container.getComponent(IDocumentRepository.class);
        this.tagRepository = container.getComponent(ITagRepository.class);
        
        if (args == null || args.length == 0) {
            return;
        }
        
        getContext().getLocalStorage().setDirectory(new File(args[0]));
    }
    
    @Override
    protected void startup() {
        getMainFrame().setJMenuBar(createMenuBar());
        getMainFrame().setIconImage(getContext().getResourceMap().getImageIcon("Application.icon").getImage());
        // TDREVIEW do you think this is a sensible solution for failing test about cancelling?
        if (restoreHomePath()) {
        
            //final JLabel label = new JLabel(this.docnapStore.getStorageLocation());
        
            show(createMainPanel());
            updateTitle();
        }
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
    private boolean restoreHomePath() {
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
        
        return (0 != getPreviousHomePath().length());
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
