package com.shensu.jsjm.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BaseProjectException extends RuntimeException {

    private static final long serialVersionUID = 3021280380538486727L;

    private Throwable         nestedThrowable  = null;

    public BaseProjectException() {
        super();
    }

    public BaseProjectException(String msg) {
        super(msg);
    }

    public BaseProjectException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public BaseProjectException(String msg, Throwable nestedThrowable) {
        super(msg);
        this.nestedThrowable = nestedThrowable;
    }

    public void printStackTrace() {
        super.printStackTrace();
        if (nestedThrowable != null) {
            nestedThrowable.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (nestedThrowable != null) {
            nestedThrowable.printStackTrace(ps);
        }
    }

    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (nestedThrowable != null) {
            nestedThrowable.printStackTrace(pw);
        }
    }
}
