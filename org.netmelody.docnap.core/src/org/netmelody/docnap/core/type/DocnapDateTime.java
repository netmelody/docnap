package org.netmelody.docnap.core.type;

import java.sql.Timestamp;
import java.util.Date;

import org.joda.time.DateTime;

public final class DocnapDateTime {

    private final DateTime value;

    public DocnapDateTime(Timestamp timestamp) {
        this.value = new DateTime(timestamp);
    }

    public Date getValueAsDate() {
        return (null == this.value) ? null : value.toDate();
    }
}
