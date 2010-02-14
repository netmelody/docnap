package org.netmelody.docnap.swingclient;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public abstract class DocnapWindow extends JFrame {
    
    private static final long serialVersionUID = 1L;
    
    private final List<PropertyChangeListener> dataChangedListeners = new ArrayList<PropertyChangeListener>();

    public DocnapWindow(String windowName) {
        setName(windowName);
    }

    public void addDataChangedListener(PropertyChangeListener dataChangedListener) {
        this.dataChangedListeners.add(dataChangedListener);
    }
    
    protected final void fireDataChangedEvent() {
        for (PropertyChangeListener dataChangedListener : dataChangedListeners) {
            dataChangedListener.propertyChange(new PropertyChangeEvent(this, "dataChanged", false, true));
        }
    }
}
