package org.netmelody.docnap.gwt.client;

import org.netmelody.docnap.gwt.client.presenter.ApplicationPresenter;
import org.netmelody.docnap.gwt.client.view.ApplicationPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class DocnapWeb implements EntryPoint {

    public void onModuleLoad() {
        final ApplicationPanel panel = new ApplicationPanel();
        new ApplicationPresenter(panel);

        Window.enableScrolling(false);
        Window.setMargin("0px");

        RootLayoutPanel root = RootLayoutPanel.get();
        root.add(panel);
    }
}
