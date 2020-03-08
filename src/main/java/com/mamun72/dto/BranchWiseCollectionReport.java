package com.mamun72.dto;

import com.mamun72.billarApi.Jgdl.JgdlConfig;

import java.util.Date;

public class BranchWiseCollectionReport {

    private String customerName;
    private String customerId;
    private String transactionId;
    private double payableAmount;
    private double billAmount;
    private double surgeCharge;
    private double stampCharge;
    private String branchName;
    private double paidAmount;
    private String paidBy;
    private Date paidAt;
    private String getBy;
    private int status;

    public BranchWiseCollectionReport(
            String customerId,
            String customerName,
            String transactionId,
            double payableAmount,
            double billAmount,
            double surgeCharge,
            double stampCharge,
            double paidAmount,
            Date paidAt,
            String paidBy,
            String branchName,
            String getBy,
            int status) {
        this.customerName = customerName;
        this.customerId = customerId;
        this.transactionId = transactionId;
        this.payableAmount = payableAmount;
        this.billAmount = billAmount;
        this.surgeCharge = surgeCharge;
        this.paidAmount = paidAmount;
        this.stampCharge = stampCharge;
        this.branchName = branchName;
        this.paidBy = paidBy;
        this.paidAt = paidAt;
        this.getBy = getBy;
        this.status = status;
    }
    public BranchWiseCollectionReport(
            String customerId,
            String customerName,
            String transactionId,
            double payableAmount,
            double billAmount,
            double surgeCharge,
            double stampCharge,
            double paidAmount,
            Date paidAt,
            String paidBy,
            String getBy) {
        this.customerName = customerName;
        this.customerId = customerId;
        this.transactionId = transactionId;
        this.payableAmount = payableAmount;
        this.billAmount = billAmount;
        this.surgeCharge = surgeCharge;
        this.paidAmount = paidAmount;
        this.stampCharge = stampCharge;
        this.paidBy = paidBy;
        this.paidAt = paidAt;
        this.getBy = getBy;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(double payableAmount) {
        this.payableAmount = payableAmount;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public double getSurgeCharge() {
        return surgeCharge;
    }

    public void setSurgeCharge(double surgeCharge) {
        this.surgeCharge = surgeCharge;
    }

    public double getStampCharge() {
        return stampCharge;
    }

    public void setStampCharge(double stampCharge) {
        this.stampCharge = stampCharge;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public Date getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Date paidAt) {
        this.paidAt = paidAt;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getGetBy() {
        return getBy;
    }

    public void setGetBy(String getBy) {
        this.getBy = getBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
