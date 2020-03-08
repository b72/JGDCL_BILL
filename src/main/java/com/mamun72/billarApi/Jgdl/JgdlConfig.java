package com.mamun72.billarApi.Jgdl;

public class JgdlConfig {

    private static final String baseUrl = "https://103.94.135.203:8083";

    private static final String userName = "nbl";
    private static final String password = "Jn@bT1D51";
    private static final String getCustomer = "/api/getBillInfo";

    private static final String payment = "/api/payment";

    private static final String getAllBills = "/api/getBillDetail";

    private static final String bankName = "NBL";

    private static final int paidStatus = 2;
    private static final int unPaidStatus = 1;
    private static final int apiSuccessCode = 200;
    private static final int billAlreadyPaidCode = 902;
    private static final int billWrongCustomerCode = 901;

    private static final double stampCharge = 10.00;

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassword() {
        return password;
    }

    public static String getGetCustomer() {
        return getCustomer;
    }

    public static String getPayment() {
        return payment;
    }

    public static String getGetAllBills() {
        return getAllBills;
    }

    public static String getBankName() {
        return bankName;
    }

    public static int getUnPaidStatus() {
        return unPaidStatus;
    }

    public static int getPaidStatus() {
        return paidStatus;
    }

    public static int getApiSuccessCode() {
        return apiSuccessCode;
    }

    public static int getBillAlreadyPaidCode() {
        return billAlreadyPaidCode;
    }

    public static int getBillWrongCustomerCode() {
        return billWrongCustomerCode;
    }

    public static double getStampCharge() {
        return stampCharge;
    }
}
