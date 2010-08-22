package org.netmelody.docnap.core.type;

import java.sql.Timestamp;
import java.util.Date;

import org.joda.time.DateTime;

public final class DocnapDateTime {

    private final DateTime value;

    public DocnapDateTime() {
        this((DateTime)null);
    }
    
    public DocnapDateTime(Timestamp timestamp) {
        this(new DateTime(timestamp));
    }
    
    public DocnapDateTime(Date date) {
        this(new DateTime(date.getTime()));
    }
    
    private DocnapDateTime(DateTime date) {
        this.value = (null == date) ? new DateTime() : date;
    }

    public Date getValueAsDate() {
        return (null == this.value) ? null : value.toDate();
    }
}
