package main.java.com.hit.server;

import java.util.Map;

/**
 * This class implements request structure.
 * Type parameters:
 * <T> – Generic type of the body requests.
 */
public class Request<T> extends java.lang.Object implements java.io.Serializable {
    private Map<String, String> headers;
    private T body;

    public Request(java.util.Map<java.lang.String, java.lang.String> headers, T body) {
        this.headers = headers;
        this.body = body;
    }

    public java.util.Map<java.lang.String, java.lang.String> getHeaders() {
        return headers;
    }

    public void setHeaders(java.util.Map<java.lang.String, java.lang.String> headers) {
        this.headers = headers;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public java.lang.String toString() {
        return getHeaders().toString().split("=")[1].toLowerCase().replace("}","");
    }


}
