package com.mamun72.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class ApiLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "IDENTIFIER", nullable = false, unique = true)
    private Long Id;

    @Column(name = "logId", length = 255, unique = false)
    private String logId;

    @Column(name = "request", length = 4000)
    private String request;

    @Column(name = "response", length = 4000)
    private String response;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

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


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "ApiLog{" +
                "Id=" + Id +
                ", logId='" + logId + '\'' +
                ", request='" + request + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
