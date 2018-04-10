package com.thewalkingschoolbus.thewalkingschoolbus.exceptions;

/*
    this is the class file for error return by the server
    if server return error code > 400 api binding throws exception
    and map each error message in to this class
 */

public class ApiException extends Exception{
    private String timestamp;
    private String status;
    private String error;
    private String message;
    private String path;

    public ApiException(String str){
        super(str);
    }

    public ApiException(String str, Throwable cause){
        super(str,cause);
    }

    public ApiException(Throwable message){
        super(message);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
