package com.yourlibrary.exceptions;

public class InvalidBookTypeException extends Exception{
    public InvalidBookTypeException(String message){
        super(message);
    }
}
