package org.netmelody.docnap.swingclient;


import javax.swing.JButton;
import javax.swing.JToolBar;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.ComponentDriver;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JComponentDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

/**
 * WindowLicker Driver for the Docnap Application.
 * 
 * @author Tom Denley
 *
 */
public class DocnapApplicationDriver extends JFrameDriver {

    @SuppressWarnings("unchecked")
    public DocnapApplicationDriver() {
        super(new GesturePerformer(), new AWTEventQueueProber(), named("mainFrame"));
    }
    
    public void clickTheIndexFileButtonOnTheToolBar() {
        toolBarButton("indexFile").click();
    }
    
    @SuppressWarnings("unchecked")
    private JButtonDriver toolBarButton(String name) {
        return new JButtonDriver(new JComponentDriver(this, JToolBar.class, ComponentDriver.named("toolBar")), JButton.class, ComponentDriver.named(name));
    }
}
