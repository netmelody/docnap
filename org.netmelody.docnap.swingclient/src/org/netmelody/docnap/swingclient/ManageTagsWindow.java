package org.netmelody.docnap.swingclient;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.netmelody.docnap.core.domain.Tag;

import com.jgoodies.binding.list.SelectionInList;

public class ManageTagsWindow extends DocnapWindow{
    
    private static final long serialVersionUID = 1L;
    
    private final ManageTagsModel model;
    
    public ManageTagsWindow(ManageTagsModel model) {
        super("manageTagsWindow");
        
        this.model = model;
        
        initialiseComponents();
    }
    
    private void initialiseComponents() {
        final JPanel panel = new JPanel();
        final JButton button = new JButton("Hello");
        
        panel.add(button);
        add(panel, BorderLayout.PAGE_START);
        
        final JTable tagTable = new JTable(new TagsTableModel());
       
        final TableCellRenderer defaultRenderer = tagTable.getDefaultRenderer(JButton.class);
        final JTableButtonRenderer buttonRenderer = new JTableButtonRenderer(defaultRenderer, this);
        tagTable.setDefaultRenderer(JButton.class, buttonRenderer);
        tagTable.getColumnModel().getColumn(1).setCellEditor(buttonRenderer);
        add(tagTable.getTableHeader());
        add(tagTable, BorderLayout.CENTER);
        tagTable.setFillsViewportHeight(true);
    }
    
    private class TagsTableModel extends AbstractTableModel {
        
        private static final long serialVersionUID = 1L;
        
        private final String[] columnNames = {"Tag", "Remove Tag"};
        private final SelectionInList<Tag> tagsModel;
        private final JButton[] removeButtons;
        
        public TagsTableModel() {
            super();
            tagsModel = model.getTagsModel();
            removeButtons = new JButton[tagsModel.getSize()];
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
            switch (col) {
                case 0:
                    return tagsModel.getElementAt(row);
                case 1:
                    if (null == removeButtons[row]) {
                        removeButtons[col] = new JButton("Remove " + tagsModel.getElementAt(row).toString());
                    }
                    return removeButtons[col];
            }
            
            return null;
        }
        
        @Override
        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        
        @Override
        public boolean isCellEditable(int row, int col)
            { return true; }
  

    }
    
    class JTableButtonRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener {
        private final TableCellRenderer defaultRenderer;
        private final Component frame;

        public JTableButtonRenderer(TableCellRenderer renderer, Component frame) {
          defaultRenderer = renderer;
          this.frame = frame;
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                               boolean isSelected,
                               boolean hasFocus,
                               int row, int column)
        {
          if(value instanceof Component)
            return (Component)value;
          return defaultRenderer.getTableCellRendererComponent(
             table, value, isSelected, hasFocus, row, column);
        }
        
        public void actionPerformed(ActionEvent e) {
            
                JOptionPane.showMessageDialog(frame, "Hello");
                
                fireEditingStopped(); //Make the renderer reappear.

            }
      //Implement the one CellEditor method that AbstractCellEditor doesn't.
        public Object getCellEditorValue() {
            return "remove";
        }

        //Implement the one method defined by TableCellEditor.
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int row,
                                                     int column) {
            return new JButton("Remove m2");
        }

        

        
        
        
      }

    

}
