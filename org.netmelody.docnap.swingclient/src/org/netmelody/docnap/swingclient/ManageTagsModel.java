package org.netmelody.docnap.swingclient;

import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.ITagRepository;

public class ManageTagsModel extends AbstractTableModel{
    
    private static final long serialVersionUID = 1L;
    
    private final ITagRepository tagRepository;
    private final List<Tag> tagsModel;
    private static final int TAG_COLUMN = 0;
    private static final int REMOVE_BUTTON_COLUMN = 1;
    
    private final String[] columnNames = {"Tag", "Remove Tag"};
    
    public ManageTagsModel(ITagRepository tagRepository) {
        super();
        
        this.tagRepository = tagRepository;
        tagsModel = this.tagRepository.fetchAll();
    }
    
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    @Override
    public int getRowCount() { 
        return tagsModel.size();
    }
    
    @Override
    public int getColumnCount(){
        return columnNames.length; 
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        if (TAG_COLUMN == col) {
            return tagsModel.get(row);
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
    
    public void removeTagAtRow(Tag tag, int row) {
        this.tagsModel.remove(tag);
        tagRepository.removeTag(tag);
        fireTableRowsDeleted(row, row);
    }

}
