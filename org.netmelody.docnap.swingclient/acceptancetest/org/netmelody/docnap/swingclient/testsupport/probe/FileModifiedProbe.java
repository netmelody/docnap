package org.netmelody.docnap.swingclient.testsupport.probe;

import java.io.File;

import org.hamcrest.Description;
import org.joda.time.DateTime;

import com.objogate.wl.Probe;

/**
 * Probe to detect if a file has been modified, satisfied by any change
 * (later or earlier) to its modification date.
 * 
 * @author tom
 *
 */
public class FileModifiedProbe implements Probe {
    private final File targetFile;
    private final DateTime originalModificationTime;
    
    private DateTime lastModificationTime = null;
    
    public FileModifiedProbe(String filename) {
        this.targetFile = new File(filename);
        this.originalModificationTime = new DateTime(this.targetFile.lastModified());
    }

    @Override
    public void describeFailureTo(Description description) {
        if (null == this.lastModificationTime) {
            description.appendText("the file did not exist ");
        }
        else {
            description.appendText("the file was last modified at ");
            description.appendText(this.lastModificationTime.toString());
            description.appendText(" ");
        }
    }

    @Override
    public boolean isSatisfied() {
        return (null != this.lastModificationTime) &&
               !this.lastModificationTime.isEqual(this.originalModificationTime);
    }

    @Override
    public void probe() {
        if (this.targetFile.isFile()) {
            this.lastModificationTime = new DateTime(this.targetFile.lastModified());
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("the file at ");
        description.appendText(this.targetFile.toString());
        description.appendText(" modified at any time other than ");
        description.appendText(this.originalModificationTime.toString());
        description.appendText(" ");
    }

}
