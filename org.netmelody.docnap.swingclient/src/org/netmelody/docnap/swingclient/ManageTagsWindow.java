package org.netmelody.docnap.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.netmelody.docnap.core.domain.Tag;

import com.jgoodies.binding.list.SelectionInList;

public class ManageTagsWindow extends DocnapWindow{
    
    private static final long serialVersionUID = 1L;
    
    private final ManageTagsModel screenModel;
    
    public ManageTagsWindow(ManageTagsModel screenModel) {
        super("manageTagsWindow");
        
        this.screenModel = screenModel;
        
        initialiseComponents();
    }
    
    private void initialiseComponents() {
        final JPanel panel = new JPanel();
        final JButton button = new JButton("Hello");
        
        panel.add(button);
        add(panel, BorderLayout.PAGE_START);
        
        final ManageTagsTableModel tagsTableModel = new ManageTagsTableModel();
        final JTable tagTable = new JTable(tagsTableModel);
        final JScrollPane tagTableScrollPane = new JScrollPane(tagTable);   
       
        final TableColumn removeButtonColumn = tagTable.getColumnModel().getColumn(tagsTableModel.getRemoveButtonColumnIndex());

        removeButtonColumn.setCellRenderer(new RemoveButtonRenderer());
        removeButtonColumn.setCellEditor(new RemoveButtonEditor());
        
       /// tagTable.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        
        
        add(tagTableScrollPane, BorderLayout.CENTER);
    }
    
    private void refreshTable() {
        
    }
    
    private class ManageTagsTableModel extends AbstractTableModel {
        
        private static final long serialVersionUID = 1L;
        private static final int TAG_COLUMN = 0;
        private static final int REMOVE_BUTTON_COLUMN = 1;
        
        private final String[] columnNames = {"Tag", "Remove Tag"};
        private final SelectionInList<Tag> tagsModel;
        
        public ManageTagsTableModel() {
            super();
            tagsModel = screenModel.getTagsModel();
        }
        
        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }
        
        @Override
        public int getRowCount() { 
            return tagsModel.getSize();
        }
        
        @Override
        public int getColumnCount(){
            return columnNames.length; 
        }
        
        @Override
        public Object getValueAt(int row, int col) {
            if (TAG_COLUMN == col) {
                return tagsModel.getElementAt(row);
            }
            
            return null;
        }
        
        @Override
        public Class<?> getColumnClass(int c) {
            if (REMOVE_BUTTON_COLUMN == c) {
                return JButton.class;
            }
            
            return getValueAt(0, c).getClass();
        }

        
        @Override
        public boolean isCellEditable(int row, int col) { 
            if (REMOVE_BUTTON_COLUMN == col) {
                return true; 
            }
            
            return false;
        }
        
        public int getRemoveButtonColumnIndex() {
            return REMOVE_BUTTON_COLUMN;
        }
        
        public int getTagColumnIndex() {
            return TAG_COLUMN;
        }
    }
    
    /**
     * @author New User
     *
     */
    private class RemoveButtonRenderer implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        
        private final JButton removeButton = new JButton("Remove");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return removeButton;
        }
    }
    
    private class RemoveButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 1L;
        
        //Implement the one method defined by TableCellEditor.
        public Component getTableCellEditorComponent(final JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     final int row,
                                                     final int column) {
            JButton editRemoveButton = new JButton("Remove");
            ManageTagsTableModel tableModel = (ManageTagsTableModel)table.getModel();
            editRemoveButton.addActionListener(
                    new RemoveTagActionListener((Tag)tableModel.getValueAt(row, tableModel.getTagColumnIndex())));
  
            return editRemoveButton;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
    
    private class RemoveTagActionListener implements ActionListener {
       
        private final Tag tag;
        
        public RemoveTagActionListener(Tag tag) {
            this.tag = tag;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            int result = JOptionPane.showConfirmDialog(null, "Remove the tag " + tag.getTitle() + " from " + tag.getDocumentCount() + " documents and remove the tag?"); 
            if (JOptionPane.OK_OPTION == result) {
                screenModel.removeTag(tag);
            }
        }
    }

}
