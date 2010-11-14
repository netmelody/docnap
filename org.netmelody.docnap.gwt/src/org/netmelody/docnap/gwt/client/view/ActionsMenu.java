package org.netmelody.docnap.gwt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ActionsMenu extends Composite {

    interface Binder extends UiBinder<Widget, ActionsMenu> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField Button addDocumentButton;
    
    public ActionsMenu() {
        initWidget(binder.createAndBindUi(this));
    }
    
    @UiHandler("addDocumentButton")
    void addDocumentClicked(ClickEvent event) {
    }
}
