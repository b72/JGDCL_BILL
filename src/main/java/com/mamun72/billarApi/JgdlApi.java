package com.mamun72.billarApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamun72.entity.ApiLog;
import com.mamun72.entity.Bill;
import com.mamun72.service.ApiLogService;
import okhttp3.*;
import okio.Buffer;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;

@Component
public class JgdlApi {


    private ApiLogService apiLogService;

    private Boolean activeLog;

    private String baseUrl;

    private String finalUrl;

    private String header;

    private String data;

    private String user;

    private String password;

    private String credentials;

    private RequestBody body;

    public JgdlApi() {
        this.baseUrl = JgdlConfig.getBaseUrl();
        this.user = JgdlConfig.getUserName();
        this.password = JgdlConfig.getPassword();
        this.activeLog = false;
        this.credentials = Credentials.basic(this.user, this.password);
    }

    public String getBillInfo(String customerId) throws Exception {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(this.baseUrl + JgdlConfig.getGetCustomer()).newBuilder();
        urlBuilder.addQueryParameter("customerId", customerId);

        this.finalUrl = urlBuilder.build().toString();
        OkHttpClient client = getUnsafeOkHttpClient();

        Request request = new Request.Builder()
                .url(this.finalUrl)
                .method("GET", null)
                .addHeader("Authorization", this.credentials)
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();

        if (this.activeLog) {
            ApiLog apiLog = new ApiLog();
            apiLog.setResponse(res);
            apiLog.setLogId(customerId);
            apiLog.setRequest(finalUrl);
            keepApiLog(apiLog);
        }
        response.body().close();
        return res;
    }

    public String payBill(PayBill payBill) throws Exception {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        String postData = mapper.writeValueAsString(payBill);
        this.body = RequestBody.create(postData, JSON);
        this.finalUrl = this.baseUrl + JgdlConfig.getPayment();
        OkHttpClient client = getUnsafeOkHttpClient();
        Request request = new Request.Builder()
                .url(this.finalUrl)
                .post(this.body)
                .addHeader("Authorization", this.credentials)
                .build();
        Response response = client.newCall(request).execute();
        String res = response.body().string();

        if (this.activeLog) {
            ApiLog apiLog = new ApiLog();
            apiLog.setResponse(res);
            apiLog.setLogId(payBill.getCustomerId().toString());
            apiLog.setRequest("{\"url\":\"" + this.finalUrl + "\", \"body\":" + bodyToString(this.body)  + "}");
            keepApiLog(apiLog);
        }

        response.body().close();
        return res;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
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
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
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

    public ApiLogService getApiLogService() {
        return apiLogService;
    }

    public void setApiLogService(ApiLogService apiLogService) {
        this.apiLogService = apiLogService;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActiveLog() {
        return activeLog;
    }

    public void setActiveLog(Boolean activeLog) {
        this.activeLog = activeLog;
    }

    public String getFinalUrl() {
        return finalUrl;
    }

    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public RequestBody getBody() {
        return body;
    }

    public void setBody(RequestBody body) {
        this.body = body;
    }

    private void keepApiLog(ApiLog apiLog) {
        apiLogService.saveLog(apiLog);
    }

    private static String bodyToString(final RequestBody request) {

        try {
            Buffer buffer = new Buffer();
            request.writeTo(buffer);
            return buffer.readUtf8();
        } catch (final Exception e) {
            return e.getMessage();
        }
    }
}
