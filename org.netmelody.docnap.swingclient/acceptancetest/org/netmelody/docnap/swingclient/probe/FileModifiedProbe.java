package org.netmelody.docnap.swingclient.probe;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.hamcrest.Description;
import org.joda.time.DateTime;

import com.objogate.wl.Probe;

public class FileModifiedProbe implements Probe {
    private final File targetFile;
    private final DateTime modificationTime;
    
    private boolean assertionMet = false;
    private boolean fileExists = false;


    
    public FileModifiedProbe(String outFilename, long modificationTime) {
        this.modificationTime = new DateTime(this.modificationTime);
        targetFile = new File(outFilename);
    }

    @Override
    public void describeFailureTo(Description description) {
        if (this.fileExists) {
            description.appendText("the file exists but was not modified recently enough ");
        }
        else {
            description.appendText("the file did not exist ");
        }
    }

    @Override
    public boolean isSatisfied() {
        return assertionMet;
    }

    @Override
    public void probe() {
        this.fileExists = this.targetFile.isFile();
        this.assertionMet = this.fileExists && (FileUtils.isFileNewer(this.targetFile, this.modificationTime.toDate()));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("the file at ");
        description.appendText(this.targetFile.toString());
        description.appendText(" was modified after ");
        description.appendText(this.modificationTime.toString());
        description.appendText(" ");
    }

}
