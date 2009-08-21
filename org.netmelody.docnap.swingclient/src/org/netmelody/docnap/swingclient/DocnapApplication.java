package org.netmelody.docnap.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.netmelody.docnap.core.domain.Document;
import org.netmelody.docnap.core.published.Bootstrap;
import org.netmelody.docnap.core.published.IDocnapStore;
import org.netmelody.docnap.core.published.IDocumentRepository;
import org.picocontainer.PicoContainer;

import com.jgoodies.binding.list.SelectionInList;

public class DocnapApplication extends SingleFrameApplication {

    private static final String SETTINGS_FILE = "lasthome.xml";

    private static final String PROPERTYNAME_DOCUMENTSELECTED = "documentSelected";

    private Bootstrap bootstrap;
    
	private IDocnapStore docnapStore;
    private IDocumentRepository documentRepository;

    private JList documentList;

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
        final DocumentWindow documentWindow = new DocumentWindow(getContext(), this.documentRepository);
        documentWindow.setDocument((Document)this.documentList.getSelectedValue());
        show(documentWindow);
    }
    
    public boolean isDocumentSelected() {
        return (null != this.documentList) && (-1 != this.documentList.getSelectedIndex());
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

    private JList createDocumentList() {
        final Collection<Document> documents = this.documentRepository.fetchAll();
        this.documentList = new JList(documents.toArray(new Document[documents.size()]));
        this.documentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.documentList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    boolean newValue = isDocumentSelected();
                    firePropertyChange(PROPERTYNAME_DOCUMENTSELECTED, !newValue, newValue);
                }
            }
        });
        
        final javax.swing.Action showDocumentAction = getAction("showDocument");
        this.documentList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (showDocumentAction.isEnabled()) {
                        showDocumentAction.actionPerformed(new ActionEvent(event.getSource(), ActionEvent.ACTION_PERFORMED, null));
                    }
                }
            }
        });
        
        return this.documentList;
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

    private JComponent createMainPanel(Component component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createToolBar(), BorderLayout.NORTH);
        panel.add(createDocumentList(), BorderLayout.WEST);
        panel.add(component, BorderLayout.CENTER);
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
        
        restoreHomePath();
        
        final JLabel label = new JLabel(this.docnapStore.getStorageLocation());
        label.setName("mainLabel");
        show(createMainPanel(label));
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
