package com.beingAbroad.eduManage.exception;

public class BadCredentialException extends RuntimeException{
    public  BadCredentialException (String message){
        super(message);
    }
}
