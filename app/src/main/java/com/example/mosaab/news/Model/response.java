package com.example.mosaab.news.Model;

public class response {

    private String response ;
    private Object result;
    private Object token;

    public response(String response, Object result, Object token) {
        this.response = response;
        this.result = result;
        this.token = token;
    }

    public response() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }
}
