package com.mamun72.billarApi;

import com.mamun72.entity.ApiLog;
import com.mamun72.service.ApiLogService;
import org.springframework.beans.factory.annotation.Autowired;

public class Logger {

    @Autowired
    ApiLogService apiLogService;


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

    public void log(){
        ApiLog apiLog = new ApiLog();
        apiLog.setLogId(this.identifier);
        apiLog.setRequest(this.request);
        apiLog.setResponse(this.response);
        System.out.println(apiLogService.saveLog(apiLog).toString());
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
