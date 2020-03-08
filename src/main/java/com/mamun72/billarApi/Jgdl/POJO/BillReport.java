package com.mamun72.billarApi.Jgdl.POJO;

public class BillReport {

    private String customerId;
    private Double paidAmount;
    private String bankName;
    private String transactionId;
    private String txndate;
    private String mobileNo;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTxndate() {
        return txndate;
    }

    public void setTxndate(String txndate) {
        this.txndate = txndate;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }


    @Override
    public String toString() {
        return "BillReport{" +
                "customerId='" + customerId + '\'' +
                ", paidAmount=" + paidAmount +
                ", bankName='" + bankName + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", txndate='" + txndate + '\'' +
                ", mobileNo=" + mobileNo +
                '}';
    }
}
