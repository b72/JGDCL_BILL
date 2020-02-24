package com.mamun72.billarApi.Jgdl;

public class JgdlConfig {

     private static final String baseUrl = "https://103.94.135.203:8083";
    //private static final String baseUrl = "https://jsonplaceholder.typicode.com";
    private static final String userName = "nbl";
    private static final String password = "Jn@bT1D51";
    private static final String getCustomer = "/api/getBillInfo";
    //private static final String getCustomer = "/comments";
    private static final String payment = "/api/payment";
    //private static final String payment = "/posts";
    private static final String getAllBills = "/api/getBillDetail";

    private static final String bankName = "NBL";

    private static final int[] paidStatus = {1,2};

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

    public static int getUnPaidStatus(){
        return paidStatus[0];
    }

    public static int getPaidStatus(){
        return paidStatus[1];
    }
}
