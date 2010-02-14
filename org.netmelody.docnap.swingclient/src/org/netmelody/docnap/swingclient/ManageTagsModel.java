package org.netmelody.docnap.swingclient;

import java.util.ArrayList;
import org.netmelody.docnap.core.domain.Tag;
import org.netmelody.docnap.core.published.ITagRepository;

import com.jgoodies.binding.list.SelectionInList;

public class ManageTagsModel {
    
    private static final long serialVersionUID = 1L;
    
    private final ITagRepository tagRepository;
    private final SelectionInList<Tag> tagsModel = new SelectionInList<Tag>();
    
    public ManageTagsModel(ITagRepository tagRepository) {
        super();
        
        this.tagRepository = tagRepository;
    }
    
    public SelectionInList<Tag> getTagsModel() {
        
        tagsModel.setList(new ArrayList<Tag>(this.tagRepository.fetchAll()));
        
        return tagsModel;
    }

}
