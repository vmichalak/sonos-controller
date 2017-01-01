package com.vmichalak.sonoscontroller.exception;

public class SonosControllerException extends Exception {
    public SonosControllerException() {
    }

    public SonosControllerException(String message) {
        super(message);
    }

    public SonosControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SonosControllerException(Throwable cause) {
        super(cause);
    }
}
