package com.mamun72.entity;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "BILL")
public class Bill {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "trxId", unique = true, nullable = false)
    private String transactionId;

    @Column(name = "customerId")
    private String customerId;

    @Column(name = "customerName", nullable = true, length = 100)
    private String customerName;

    @Column(name = "monYear", nullable = true, length = 255)
    private String monYear;

    @Column(name = "billAmount", nullable = true, precision = 2)
    private Double billAmount;

    @Column(name = "surcharge", nullable = true, precision = 2)
    private Double surcharge;

    @Column(name = "paybleAmount", nullable = true, precision = 2)
    private Double paybleAmount;

    @Column(name = "billCount", nullable = true)
    private Integer billcount;

    @Column(name = "status", nullable = true, length = 2)
    private Integer status;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "createdAt", nullable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "updatedAt", nullable = true)
    private Date updatedAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "paidAt", nullable = true)
    private Date paidAt;

    private String paidBy;

    private Double stampCharge;

    private String mobileNo;
    private double paidAmount;
    private String bankName;
    private String jsdclTrxId;

    private String branchCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = true, updatable = true)
    @Fetch(FetchMode.JOIN)
    private User user;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMonYear() {
        return monYear;
    }

    public void setMonYear(String monYear) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getJsdclTrxId() {
        return jsdclTrxId;
    }

    public void setJsdclTrxId(String jsdclTrxId) {
        this.jsdclTrxId = jsdclTrxId;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }


    public Double getStampCharge() {
        return stampCharge;
    }

    public void setStampCharge(Double stampCharge) {
        this.stampCharge = stampCharge;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "transactionId='" + transactionId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", monYear='" + monYear + '\'' +
                ", billAmount=" + billAmount +
                ", surcharge=" + surcharge +
                ", paybleAmount=" + paybleAmount +
                ", billcount=" + billcount +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", paidAt=" + paidAt +
                ", paidBy='" + paidBy + '\'' +
                ", stampCharge=" + stampCharge +
                ", mobileNo='" + mobileNo + '\'' +
                ", paidAmount=" + paidAmount +
                ", bankName='" + bankName + '\'' +
                ", jsdclTrxId='" + jsdclTrxId + '\'' +
                ", branchCode='" + branchCode + '\'' +
                ", user=" + user +
                '}';
    }
}
