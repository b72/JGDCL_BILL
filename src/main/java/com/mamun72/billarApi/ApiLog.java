package com.mamun72.billarApi;

public class ApiLog {

    private String request;
    private String response;
    private String identifier;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "ApiLog{" +
                "request='" + request + '\'' +
                ", response='" + response + '\'' +
                ", identifier='" + identifier + '\'' +
                '}';
    }

    public String toJson() {
        return "{" + "request:" + request + ", response:" + response + ", identifier:" + identifier + "}";
    }
}
