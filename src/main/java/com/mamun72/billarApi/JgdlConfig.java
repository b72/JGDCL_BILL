package com.mamun72.billarApi;

class JgdlConfig {

    // private static final String baseUrl = "https://103.94.135.203:8083";
    private static final String baseUrl = "https://jsonplaceholder.typicode.com";
    private static final String userName = "nbl";
    private static final String password = "Jn@bT1D51";
    //private static final String getCustomer = "/api/getBillInfo";
    private static final String getCustomer = "/comments";
    //private static final String payment = "/api/payment";
    private static final String payment = "/posts";
    private static final String getAllBills = "/post";

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
}
