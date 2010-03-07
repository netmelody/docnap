package org.netmelody.docnap.swingclient.testsupport.driver;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JToolBar;

import org.hamcrest.Matcher;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.ComponentDriver;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JComponentDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JListDriver;
import com.objogate.wl.swing.driver.JMenuDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import com.objogate.wl.swing.matcher.JLabelTextMatcher;

/**
 * WindowLicker Driver for the Docnap Application.
 * 
 * @author Tom Denley
 *
 */
public final class DocnapApplicationDriver extends JFrameDriver {

    @SuppressWarnings("unchecked")
    private DocnapApplicationDriver() {
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
    
    public void clickTheShowFileButtonOnTheToolBar() {
        toolBarButton("showDocument").click();
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
    
    public void clickTheFileSelectHomeDirectoryMenuOption() {
        final JMenuDriver fileMenu = menuBar().menu(named("fileMenu"));
        fileMenu.click();
        fileMenu.menuItem(named("chooseHomeDirectoryMenuItem")).click();
    }
    
    public void selectTheTagCalled(String tagName) {
        @SuppressWarnings("unchecked")
        final JListDriver tagList = new JListDriver(this, JList.class, ComponentDriver.named("tagList"));
        tagList.selectItem(JLabelTextMatcher.withLabelText(startsWith(tagName)));
    }
    
    public void selectTheDocumentTitled(String documentTitle) {
        @SuppressWarnings("unchecked")
        final JListDriver documentList = new JListDriver(this, JList.class, ComponentDriver.named("documentList"));
        documentList.selectItem(JLabelTextMatcher.withLabelText(documentTitle));
    }
    
    
    // Checking methods
    
    @SuppressWarnings("unchecked")
    public void hasClosed() {
        assertFalse("Frame was not closed.", JFrameDriver.topLevelFrame(ComponentDriver.named("mainFrame")).isSatisfied());
    }
    
    public void hasNoTagTitled(String tagName) {
        @SuppressWarnings("unchecked")
        final JListDriver tagList = new JListDriver(this, JList.class, ComponentDriver.named("tagList"));
        tagList.is(showingOnScreen());
        tagList.selectNothing();
        tagList.selectItem(JLabelTextMatcher.withLabelText(startsWith(tagName)));
        tagList.hasSelectedIndex(-1);
    }
    
    public void hasNoDocumentTitled(String documentTitle) {
        selectTheTagCalled("All");
        @SuppressWarnings("unchecked")
        final JListDriver documentList = new JListDriver(this, JList.class, ComponentDriver.named("documentList"));
        documentList.is(showingOnScreen());
        documentList.selectNothing();
        documentList.selectItem(JLabelTextMatcher.withLabelText(documentTitle));
        documentList.hasSelectedIndex(-1);
    }
    
    public void hasTagTitled(String tagName) {
        @SuppressWarnings("unchecked")
        final JListDriver tagList = new JListDriver(this, JList.class, ComponentDriver.named("tagList"));
        tagList.is(showingOnScreen());
        tagList.hasItem(startsWith(tagName));
    }
    
    @SuppressWarnings("unchecked")
    public void hasDocumentTitled(String documentTitle) {
        selectTheTagCalled("All");
        final JListDriver documentList = new JListDriver(this, JList.class, ComponentDriver.named("documentList"));
        documentList.is(showingOnScreen());

        documentList.hasItem((Matcher<String>)equalTo(documentTitle));
    }
}
