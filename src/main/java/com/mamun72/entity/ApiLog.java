package com.mamun72.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "API_LOG")
public class ApiLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ApiLogIdGenerator")
    @SequenceGenerator(name = "ApiLogIdGenerator")
    private long Id;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "createdAt", nullable = false, unique = true)
    private Date createdAt;

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

    public void setId(long id) {
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
