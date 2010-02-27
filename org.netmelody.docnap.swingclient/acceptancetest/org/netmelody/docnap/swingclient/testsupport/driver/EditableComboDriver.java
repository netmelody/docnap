package org.netmelody.docnap.swingclient.testsupport.driver;

import static com.objogate.wl.gesture.Gestures.pressKey;
import static com.objogate.wl.gesture.Gestures.releaseKey;
import static com.objogate.wl.gesture.Gestures.type;
import static com.objogate.wl.gesture.Gestures.withModifierMask;

import java.awt.Component;

import javax.swing.JComboBox;
import javax.swing.KeyStroke;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.objogate.wl.Automaton;
import com.objogate.wl.Gesture;
import com.objogate.wl.Prober;
import com.objogate.wl.swing.ComponentSelector;
import com.objogate.wl.swing.driver.ComponentDriver;
import com.objogate.wl.swing.driver.JComboBoxDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;

/**
 * TODO: This is to get around the fact that WindowLicker doesn't yet support editable combo boxes.
 * 
 * When WindowLicker adds support, replace usage with JComboBoxDriver.
 * 
 * @author tom
 *
 */
public class EditableComboDriver extends JComboBoxDriver {

    public EditableComboDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JComboBox> componentType, Matcher<? super JComboBox>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public EditableComboDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JComboBox> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public EditableComboDriver(GesturePerformer gesturePerformer, ComponentSelector<JComboBox> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public void replaceAllText(String text) {
        leftClickOnComponent();
        selectAllText();
        performGesture(type(text));
    }
    
    private void selectAllText() {
        performGesture(new Gesture() {
            public void describeTo(Description description) {
                description.appendText("select-all");
            }

            public void performGesture(Automaton automaton) {
                final KeyStroke selectAll = KeyStroke.getKeyStroke("ctrl pressed A");
                automaton.perform(withModifierMask(selectAll.getModifiers(),
                        selectAll.isOnKeyRelease()
                                ? releaseKey(selectAll.getKeyCode())
                                : pressKey(selectAll.getKeyCode())));
                
            }
        });
    }
}
