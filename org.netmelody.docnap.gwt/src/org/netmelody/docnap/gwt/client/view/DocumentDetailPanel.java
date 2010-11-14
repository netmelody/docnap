package org.netmelody.docnap.gwt.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

public class DocumentDetailPanel extends ResizeComposite {

    interface Binder extends UiBinder<Widget, DocumentDetailPanel> { }
    private static final Binder binder = GWT.create(Binder.class);
    
    private final DateTimeFormat dateFormatter = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM);

    @UiField Element titleSpan;
    @UiField HTML description;
    @UiField Image documentImage;
    
    public DocumentDetailPanel() {
        initWidget(binder.createAndBindUi(this));
    }
}
