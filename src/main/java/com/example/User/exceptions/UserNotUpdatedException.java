package com.example.User.exceptions;

public class UserNotUpdatedException extends RuntimeException{
    public UserNotUpdatedException(String msg) {
        super(msg);
    }
}
