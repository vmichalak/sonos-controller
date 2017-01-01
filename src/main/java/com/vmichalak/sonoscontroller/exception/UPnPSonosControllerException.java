package com.vmichalak.sonoscontroller.exception;

public class UPnPSonosControllerException extends SonosControllerException {
    private final int errorCode;
    private final String errorDescription;
    private final String response;

    public UPnPSonosControllerException(int errorCode, String errorDescription, String response) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.response = response;
    }

    public UPnPSonosControllerException(String message, int errorCode, String errorDescription, String response) {
        super(message);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.response = response;
    }

    public UPnPSonosControllerException(String message, Throwable cause, int errorCode, String errorDescription, String response) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.response = response;
    }

    public UPnPSonosControllerException(Throwable cause, int errorCode, String errorDescription, String response) {
        super(cause);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.response = response;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getResponse() {
        return response;
    }
}
