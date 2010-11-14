package org.netmelody.docnap.gwt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;

public final class ApplicationPanel extends Composite implements ApplicationView {

    interface Binder extends UiBinder<DockLayoutPanel, ApplicationPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField TopPanel topPanel;
    @UiField Navigator navigator;
    @UiField DocumentListPanel documentListPanel;
    @UiField DocumentDetailPanel documentDetailPanel;
    
    public ApplicationPanel() {
        DockLayoutPanel outer = binder.createAndBindUi(this);
        Element topElem = outer.getWidgetContainerElement(topPanel);
        topElem.getStyle().setZIndex(2);
        topElem.getStyle().setOverflow(Overflow.VISIBLE);
        initWidget(outer);
    }
    
    @UiFactory TopPanel makeTopPanel() { return new TopPanel(); }
    @UiFactory Navigator makeNavigator() { return new Navigator(); }
    @UiFactory DocumentListPanel makeDocumentListPanel() { return new DocumentListPanel(); }
    @UiFactory DocumentDetailPanel makeDocumentDetailPanel() { return new DocumentDetailPanel(); }
}
