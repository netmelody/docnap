package org.netmelody.docnap.swingclient;


import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.hamcrest.Matcher;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.ComponentDriver;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JComponentDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JMenuDriver;
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
    
    public static DocnapApplicationDriver docnapApplicationShowingMainFrameOnScreen() {
        final DocnapApplicationDriver driver = new DocnapApplicationDriver();
        driver.is(showingOnScreen());
        return driver;
    }
    
    public static DocnapApplicationDriver docnapApplicationNotShowingMainFrameOnScreen() {
        final DocnapApplicationDriver driver = new DocnapApplicationDriver();
        driver.is(not(showingOnScreen()));
        return driver;
    }
    
    
    // Doing methods
    
    public void clickTheIndexFileButtonOnTheToolBar() {
        toolBarButton("indexFile").click();
    }
    
    @SuppressWarnings("unchecked")
    private JButtonDriver toolBarButton(String name) {
        return new JButtonDriver(new JComponentDriver(this, JToolBar.class, ComponentDriver.named("toolBar")), JButton.class, ComponentDriver.named(name));
    }

    public void clickTheFileExitMenuOption() {
        final JMenuDriver fileMenu = menuBar().menu(named("fileMenu"));
        fileMenu.click();
        fileMenu.menuItem(named("quitMenuItem")).click();
    }
    
    
    // Checking methods
    
    @SuppressWarnings("unchecked")
    public void hasClosed() {
        assertFalse("Frame was not closed.", JFrameDriver.topLevelFrame(ComponentDriver.named("mainFrame")).isSatisfied());
    }
}
