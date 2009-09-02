package org.netmelody.docnap.swingclient.controls;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import org.jdesktop.application.Action;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.ITagRepository;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

public class TagBar extends JToolBar{

    private static final long serialVersionUID = 1L;
    private static final String CUSTOMKEY_TAG = "key";
    
    private final ITagRepository tagRepository;
    private final ApplicationActionMap applicationActionMap;
    
    private Integer documentId;
    private ValueModel newTagModel = new ValueHolder();

    public TagBar(final ApplicationContext applicationContext, final ITagRepository tagRepository) {
        super();
        this.tagRepository = tagRepository;
        setRollover(true);
        setFloatable(false);
        
        this.applicationActionMap = applicationContext.getActionMap(this);
    }

    public void setDocumentId(final Integer documentId) {
        this.documentId = documentId;
        refresh();
    }

    public Integer getDocumentId() {
        return documentId;
    }
    
    private void refresh() {
        removeAll();
        if (null == this.documentId) {
            return;
        }

        final Collection<Tag> tags = this.tagRepository.findByDocumentId(this.documentId);
        for (Tag tag : tags) {
            addTagButton(tag);
        }
        
        add(BasicComponentFactory.createTextField(this.newTagModel));
        add(this.applicationActionMap.get("addTag"));
        validate();
    }

    private void addTagButton(Tag tag) {
        final JToolBar subBar = new JToolBar();
        subBar.setRollover(true);
        subBar.setFloatable(false);
        subBar.putClientProperty(CUSTOMKEY_TAG, tag);
        
        final JButton tagButton = new JButton(tag.getTitle());
        tagButton.setFocusable(false);
        subBar.add(tagButton);
        subBar.add(new JToolBar.Separator(new Dimension(1, 15)), 1);
        
        final JButton removeButton = new JButton();
        removeButton.setAction(this.applicationActionMap.get("removeTag"));
        removeButton.putClientProperty(CUSTOMKEY_TAG, tag);
        removeButton.setFocusable(false);
        subBar.add(removeButton, 2);
        subBar.add(Box.createHorizontalStrut(5), 3);
        add(subBar, 0);
    }
    
    @Action
    public void addTag(ActionEvent event) {
        final String newTagLabel = (String)this.newTagModel.getValue();
        if (null == newTagLabel || 0 == newTagLabel.length()) {
            return;
        }
        final Tag newTag = this.tagRepository.tagDocumentById(this.documentId, newTagLabel);
        
        // Check that the tag isn't already being displayed.
        final Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JToolBar) {
                final Tag tag = (Tag)((JToolBar)component).getClientProperty(CUSTOMKEY_TAG);
                if (newTag.getIdentity().equals(tag.getIdentity())) {
                    return;
                }
            }
        }
        addTagButton(newTag);
        validate();
    }
    
    @Action
    public void removeTag(ActionEvent event) {
        final JComponent source = (JComponent)event.getSource();
        final Tag tag = (Tag)source.getClientProperty(CUSTOMKEY_TAG);
        this.tagRepository.unTagDocumentById(this.documentId, tag.getTitle());
        remove(source.getParent());
        validate();
    }
}
