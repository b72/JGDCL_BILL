package com.mamun72.billarApi.Jgdl.POJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class BillResponse {

    @JsonIgnoreProperties(ignoreUnknown = true)

    private String customerId;
    private String customerName;
    private String monyear;
    private Double billAmount;
    private Double paybleAmount;
    private int billcount;
    private String transactionId;
    private Double surcharge;

    //Added property not in api
    private Double stampCharge;

    private int status;


    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setMonyear(String monyear) {
        this.monyear = monyear;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public void setPaybleAmount(Double paybleAmount) {
        this.paybleAmount = paybleAmount;
    }

    public void setBillcount(int billcount) {
        this.billcount = billcount;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setSurcharge(Double surcharge) {
        this.surcharge = surcharge;
    }

    public void setStampCharge(Double stampCharge) {
        this.stampCharge = stampCharge;
    }


    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getMonyear() {
        return monyear;
    }

    public Double getBillAmount() {
        return billAmount;
    }

    public Double getPaybleAmount() {
        return paybleAmount;
    }

    public int getBillcount() {
        return billcount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Double getSurcharge() {
        return surcharge;
    }

    public Double getStampCharge() {
        return stampCharge;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "BillResponse{" +
                "customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", monyear='" + monyear + '\'' +
                ", billAmount=" + billAmount +
                ", paybleAmount=" + paybleAmount +
                ", billcount=" + billcount +
                ", transactionId='" + transactionId + '\'' +
                ", surcharge=" + surcharge +
                ", stampCharge=" + stampCharge +
                ", status=" + status +
                '}';
    }
}
