package org.netmelody.docnap.core.exception;

public class DocnapRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs an <code>DocnapRuntimeException</code> with the 
     * specified detail message. 
     *
     * @param message
     *     the detail message.
     */
    public DocnapRuntimeException(String message) {
        this(message, null);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.</p>
     *
     * @param message
     *     the detail message (which is saved for later retrieval
     *     by the {@link Throwable#getMessage()} method).
     * @param cause
     *     the cause (which is saved for later retrieval by the
     *     {@link Throwable#getCause()} method). (A <code>null</code> value
     *     is permitted, and indicates that the cause is nonexistent or
     *     unknown.)
     */
    public DocnapRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
