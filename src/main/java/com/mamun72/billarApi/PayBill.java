package com.mamun72.billarApi;

public class PayBill {
    private String customerId;
    private Double paidAmount;
    private String bankName;
    private String transactionId;
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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @Override
    public String toString() {
        return "PayBill{" +
                "customerId='" + customerId + '\'' +
                ", paidAmount=" + paidAmount +
                ", bankName='" + bankName + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                '}';
    }
}
