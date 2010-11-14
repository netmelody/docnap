package org.netmelody.docnap.gwt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

class DocumentListNavBar extends Composite {
    
    interface Binder extends UiBinder<Widget, DocumentListNavBar> { }
    private static final Binder binder = GWT.create(Binder.class);
    
    private final DocumentListPanel outer;

    @UiField Element countLabel;
    @UiField Anchor previousButton;
    @UiField Anchor nextButton;

    public DocumentListNavBar(DocumentListPanel outer) {
        initWidget(binder.createAndBindUi(this));
        this.outer = outer;
    }

    public void update(int startIndex, int count, int max) {
        setVisibility(previousButton, startIndex != 0);
        setVisibility(nextButton, max < count);
        countLabel.setInnerText("" + Math.min(startIndex + 1, max) + " - " + max + " of " + count);
    }

    @UiHandler("previousButton")
    void onPreviousClicked(ClickEvent event) {
        outer.previous();
    }

    @UiHandler("nextButton")
    void onNextClicked(ClickEvent event) {
        outer.next();
    }

    private void setVisibility(Widget widget, boolean visible) {
        widget.getElement().getStyle().setVisibility(
                visible ? Visibility.VISIBLE : Visibility.HIDDEN);
    }
}
