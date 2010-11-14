package org.netmelody.docnap.gwt.client.view;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

public class DocumentListPanel extends ResizeComposite {

    interface Binder extends UiBinder<Widget, DocumentListPanel> { }
    private static final Binder binder = GWT.create(Binder.class);
    
    private static final int VISIBLE_DOCUMENT_COUNT = 20;
    
    private final DateTimeFormat dateFormatter = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_MEDIUM);
    private final ArrayList<Object> displayedDocuments = new ArrayList<Object>();
    private int matchingDocumentCount = 0;
    private int startIndex = 0;
    
    private DocumentListNavBar documentListNavBar;

    @UiField FlexTable header;
    @UiField FlexTable table;
    @UiField SelectionStyle selectionStyle;

    interface SelectionStyle extends CssResource {
        String selectedRow();
    }

    public DocumentListPanel() {
        initWidget(binder.createAndBindUi(this));
        documentListNavBar = new DocumentListNavBar(this);
        initTable();
    }
    
    private void initTable() {
        header.getColumnFormatter().setWidth(0, "384px");
        header.getColumnFormatter().setWidth(1, "160px");
        header.getColumnFormatter().setWidth(2, "128px");
        header.getColumnFormatter().setWidth(4, "256px");
        
        header.setText(0, 0, "Title");
        header.setText(0, 1, "Tags");
        header.setText(0, 2, "");
        header.setText(0, 3, "");
        header.setWidget(0, 4, documentListNavBar);
        header.getCellFormatter().setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT);
        
        table.getColumnFormatter().setWidth(0, "384px");
        table.getColumnFormatter().setWidth(1, "160px");
        table.getColumnFormatter().setWidth(2, "128px");
    }

    @Override
    protected void onLoad() {
        update();
    }

    public void previous() {
        startIndex = Math.max(startIndex - VISIBLE_DOCUMENT_COUNT, 0);
        update();
    }

    public void next() {
        startIndex = Math.min(startIndex + VISIBLE_DOCUMENT_COUNT, matchingDocumentCount);
        update();
    }

    @UiHandler("table")
    void onTableClicked(ClickEvent event) {
        Cell cell = table.getCellForEvent(event);
        if (cell != null) {
            selectRow(cell.getRowIndex());
        }
    }

    private void selectRow(int row) {
        for(int i = 0; i < table.getRowCount(); i++) {
            styleRow(i, false);
        }
        
        if (row < 0 || row >= displayedDocuments.size()) {
            return;
        }
        styleRow(row, true);
    }

    private void styleRow(int row, boolean selected) {
        if (row != -1) {
            String style = selectionStyle.selectedRow();
            if (selected) {
                table.getRowFormatter().addStyleName(row, style);
            } else {
                table.getRowFormatter().removeStyleName(row, style);
            }
        }
    }

    private void update() {
    }
}
