
package org.com.rarp.interfaces;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.12
 * 2017-12-01T15:19:15.565-02:00
 * Generated source version: 3.1.12
 */

@SuppressWarnings("serial")
@WebFault(name = "Exception", targetNamespace = "http://interfaces.rarp.com.org/")
public class Exception_Exception extends java.lang.Exception {
    
    private org.com.rarp.interfaces.Exception exception;

    public Exception_Exception() {
        super();
    }
    
    public Exception_Exception(String message) {
        super(message);
    }
    
    public Exception_Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public Exception_Exception(String message, org.com.rarp.interfaces.Exception exception) {
        super(message);
        this.exception = exception;
    }

    public Exception_Exception(String message, org.com.rarp.interfaces.Exception exception, Throwable cause) {
        super(message, cause);
        this.exception = exception;
    }

    public org.com.rarp.interfaces.Exception getFaultInfo() {
        return this.exception;
    }
}
