package org.netmelody.docnap.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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
import org.netmelody.docnap.core.domain.DocnapStore;
import org.netmelody.docnap.core.exception.DocnapRuntimeException;

public class DocnapApplication extends SingleFrameApplication {

    private static final String SETTINGS_FILE = "lasthome.xml";
	private DocnapStore docnapStore;

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
				throw new DocnapRuntimeException("Failed to recognise chosen path [" + file + "].", exception);
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
            this.docnapStore.addDocument(file);
        }
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

    private JComponent createToolBar() {
        String[] toolbarActionNames = {
                "indexFile",
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
        panel.add(component, BorderLayout.CENTER);
        panel.setBorder(new EmptyBorder(0, 2, 2, 2)); // top, left, bottom, right
        panel.setPreferredSize(new Dimension(640, 480));
        return panel;
    }
    
    @Override
    protected void startup() {
        getMainFrame().setJMenuBar(createMenuBar());
        
        this.docnapStore = new DocnapStore();
        restoreHomePath();
        
        final JLabel label = new JLabel(this.docnapStore.getStorageLocation());
        label.setName("mainLabel");
        show(createMainPanel(label));
    }

    @Override
    protected void shutdown() {
        this.docnapStore.shutDown();
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
