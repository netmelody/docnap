package org.netmelody.docnap.gwt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class TopPanel extends Composite {
    
    interface Binder extends UiBinder<Widget, TopPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField TextBox searchTextBox;
    @UiField Button searchButton;
    @UiField Anchor signOutLink;
    @UiField Anchor aboutLink;
    @UiField Label welcomeMessageLabel;

    public TopPanel() {
        initWidget(binder.createAndBindUi(this));
    }

    @UiHandler("aboutLink")
    void onAboutClicked(ClickEvent event) {
        AboutDialog dlg = new AboutDialog();
        dlg.show();
        dlg.center();
    }

    @UiHandler("signOutLink")
    void onSignOutClicked(ClickEvent event) {
    }
    
    @UiHandler("searchTextBox")
    void searchTextBoxAction(KeyDownEvent event) {
    }
    
    @UiHandler("searchButton")
    void searchButtonClicked(ClickEvent event) {
    }
}
