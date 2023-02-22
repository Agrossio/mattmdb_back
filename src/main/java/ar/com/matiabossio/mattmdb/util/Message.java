package ar.com.matiabossio.mattmdb.util;

public class Message {
    private String message;
    private Integer statusCode;
    private Boolean ok;
    private Object response;

    public Message(String message, Integer statusCode, Boolean ok, Object response) {
        this.message = message;
        this.statusCode = statusCode;
        this.ok = ok;
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }
}
