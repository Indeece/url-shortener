package ru.indeece.urlshortener.exceptions;

public class MissingUrlException extends RuntimeException {

    public MissingUrlException(String message) {
        super(message);
    }

}