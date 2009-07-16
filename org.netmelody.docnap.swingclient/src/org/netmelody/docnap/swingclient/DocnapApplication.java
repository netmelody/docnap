package org.netmelody.docnap.swingclient;

import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;

public class DocnapApplication extends SingleFrameApplication {

    private javax.swing.Action getAction(String actionName) {
        return getContext().getActionMap().get(actionName);
    }
    
    @Action
    public void chooseHomeDirectory(ActionEvent e) {
        final JFileChooser dirChooser = new JFileChooser(); 
        dirChooser.setDialogTitle("");
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setAcceptAllFileFilterUsed(false);

        if (dirChooser.showOpenDialog(getMainFrame()) == JFileChooser.APPROVE_OPTION) { 
            dirChooser.getSelectedFile();
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
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        String[] fileMenuActionNames = {"chooseHomeDirectory"};
        menuBar.add(createMenu("fileMenu", fileMenuActionNames));
        return menuBar;
    }
    
    @Override
    protected void startup() {
        getMainFrame().setJMenuBar(createMenuBar());
        JLabel label = new JLabel();
        label.setName("mainLabel");
        show(label);
    }

}
