package com.shensu.jsjm.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class BaseException extends RuntimeException {
    private static final long serialVersionUID = -2944390553776751994L;

    private Throwable         nestedThrowable  = null;

    public BaseException() {
        super();
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(Throwable nestedThrowable) {
        this.nestedThrowable = nestedThrowable;
    }

    public BaseException(String msg, Throwable nestedThrowable) {
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
