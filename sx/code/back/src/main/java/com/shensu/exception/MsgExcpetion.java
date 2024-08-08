package com.shensu.exception;

public class MsgExcpetion extends RuntimeException{
    public MsgExcpetion() {
        super();
    }

    public MsgExcpetion(String message) {
        super(message);
    }
}
