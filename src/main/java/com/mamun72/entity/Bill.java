package com.mamun72.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Bill {
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    @Column(name = "customerId")
    private Long customerId;

    @Column(name = "customerName", nullable = true, length = 100)
    private String customerName;

    @Column(name = "monYear", nullable = true, length = 100)
    private Date monYear;

    @Column(name = "billAmount", nullable = true, precision = 2)
    private Double billAmount;

    @Column(name = "surcharge", nullable = true, precision = 2)
    private Double surcharge;

    @Column(name = "paybleAmount", nullable = true, precision = 2)
    private Double paybleAmount;

    @Column(name = "billcount", nullable = true)
    private Integer billcount;

    @Column(name = "status", nullable = true, length = 50)
    private String status;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getMonYear() {
        return monYear;
    }

    public void setMonYear(Date monYear) {
        this.monYear = monYear;
    }

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public Double getSurcharge() {
        return surcharge;
    }

    public void setSurcharge(Double surcharge) {
        this.surcharge = surcharge;
    }

    public Double getPaybleAmount() {
        return paybleAmount;
    }

    public void setPaybleAmount(Double paybleAmount) {
        this.paybleAmount = paybleAmount;
    }

    public Integer getBillcount() {
        return billcount;
    }

    public void setBillcount(Integer billcount) {
        this.billcount = billcount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", monYear='" + monYear + '\'' +
                ", billAmount=" + billAmount +
                ", surcharge=" + surcharge +
                ", paybleAmount=" + paybleAmount +
                ", billcount=" + billcount +
                ", status='" + status + '\'' +
                '}';
    }
}
