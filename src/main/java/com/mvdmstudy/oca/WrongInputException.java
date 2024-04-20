package com.mvdmstudy.oca;

public class WrongInputException extends RuntimeException {
    public WrongInputException(String text) {
        super(text);
    }
}
