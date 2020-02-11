package com.mamun72.billarApi;

import okhttp3.*;

import javax.net.ssl.*;
import java.security.cert.CertificateException;


public class JgdlApi {

    private String baseUrl;

    private String header;

    private String data;

    private String user;

    private String password;

    public JgdlApi(){
        this.baseUrl = "https://103.94.135.203:8083/api/";
        this.user = "nbl";
        this.password = "Jn@bT1D51";
    }

    public String getBillInfo(String customerId) throws Exception {
        String finalUrl = this.baseUrl + "getBillInfo?customerId=" + customerId;
        OkHttpClient client = getUnsafeOkHttpClient();
        Request request = new Request.Builder()
                .url(finalUrl)
                .method("GET", null)
                .addHeader("Authorization", "Basic bmJsOkpuQGJUMUQ1MQ==")
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        response.body().close();
        return res;
    }

    public String payBill(PayBill payBill) throws Exception {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(payBill.toString(), JSON);
        String finalUrl = this.baseUrl + "api/payment";
        OkHttpClient client = getUnsafeOkHttpClient();
        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .addHeader("Authorization", "Basic bmJsOkpuQGJUMUQ1MQ==")
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();
        response.body().close();
        return res;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void keepApiLog()
    {
    }
}
