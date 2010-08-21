package org.netmelody.docnap.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.netmelody.docnap.core.domain.Tag;

public class ManageTagsWindow extends DocnapWindow {
    
    private static final long serialVersionUID = 1L;
    private final ManageTagsModel screenModel;
    
    public ManageTagsWindow(ManageTagsModel screenModel) {
        super("manageTagsWindow");
        this.screenModel = screenModel;
        initialiseComponents();
    }
    
    private void initialiseComponents() {
        final JTable tagTable = new JTable(screenModel);
        final JScrollPane tagTableScrollPane = new JScrollPane(tagTable);   
        final TableColumn removeButtonColumn = tagTable.getColumnModel().getColumn(screenModel.getRemoveButtonColumnIndex());

        removeButtonColumn.setCellRenderer(new RemoveButtonRenderer());
        removeButtonColumn.setCellEditor(new RemoveButtonEditor());
        
        add(tagTableScrollPane, BorderLayout.CENTER);
    }
    
    private static class RemoveButtonRenderer implements TableCellRenderer {
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
            ManageTagsModel tableModel = (ManageTagsModel)table.getModel();
            editRemoveButton.addActionListener(
                    new RemoveTagActionListener((Tag)tableModel.getValueAt(row, tableModel.getTagColumnIndex()), row));
  
            return editRemoveButton;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
    
    private class RemoveTagActionListener implements ActionListener {
       
        private final Tag tag;
        private final int row;
        
        public RemoveTagActionListener(Tag tag, int row) {
            this.tag = tag;
            this.row = row;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            int result = JOptionPane.showOptionDialog(null,
                                                      "Permanently remove the tag \"" + tag.getTitle() +
                                                      "\" (applied to " + tag.getDocumentCount() + " documents)?",
                                                      "Confirm Tag Deletion",
                                                      JOptionPane.YES_NO_OPTION,
                                                      JOptionPane.QUESTION_MESSAGE,
                                                      null,
                                                      new String[]{"Delete Tag", "Keep Tag"},
                                                      "Delete Tag"); 
            if (0 == result) {
                screenModel.removeTagAtRow(tag, row);
                fireDataChangedEvent();
            }
        }
    }

}
