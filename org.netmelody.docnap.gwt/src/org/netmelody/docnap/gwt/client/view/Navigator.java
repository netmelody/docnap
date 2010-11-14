package org.netmelody.docnap.gwt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.StackLayoutPanel;

public class Navigator extends ResizeComposite {

    interface Binder extends UiBinder<StackLayoutPanel, Navigator> { }
    private static final Binder binder = GWT.create(Binder.class);
    
    @UiField TagsMenu tagsMenu;
    @UiField ActionsMenu actonsMenu;

    public Navigator() {
        initWidget(binder.createAndBindUi(this));
    }
    
    @UiFactory TagsMenu makeTagsMenu() { return new TagsMenu(); }
    @UiFactory ActionsMenu makeActionsMenu() { return new ActionsMenu(); }
}
