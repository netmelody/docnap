package org.netmelody.docnap.swingclient;

import java.util.Date;

import org.netmelody.docnap.core.type.DocnapDateTime;

import com.jgoodies.binding.value.AbstractConverter;
import com.jgoodies.binding.value.ValueModel;

public final class DocnapDateTimeConverter extends AbstractConverter {

    public DocnapDateTimeConverter(ValueModel subject) {
        super(subject);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public void setValue(Object newValue) {
        if (null == newValue) {
            subject.setValue(null);
            return;
        }
        subject.setValue(new DocnapDateTime((Date)newValue));
    }

    @Override
    public Object convertFromSubject(Object subjectValue) {
        if (null == subjectValue) {
            return null;
        }
        return ((DocnapDateTime)subjectValue).getValueAsDate();
    }

}
