package com.xavier.pastebin.exceptions;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class DataSerializationException extends RuntimeException   {
    public DataSerializationException(String message) {
        super(message);
    }

    public DataSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSerializationException(Throwable cause) {
        super(cause);
    }

    public DataSerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
