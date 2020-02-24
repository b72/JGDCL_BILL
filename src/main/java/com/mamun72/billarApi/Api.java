package com.mamun72.billarApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okio.Buffer;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.HashMap;

public class Api implements ApiRequest{


    private String baseUrl;

    private Headers headers;

    private String response;

    private RequestBody requestBody;

    private String logId;



    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public String getResponse() {
        return response;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
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

    @Override
    public String getRequest(HashMap<String, String> queryParam) throws IOException {

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(this.baseUrl).newBuilder();

        queryParam.forEach((key, value) -> urlBuilder.addQueryParameter(key, value));

        this.baseUrl = urlBuilder.build().toString();
        OkHttpClient client = getUnsafeOkHttpClient();

        Request request = new Request.Builder()
                .url(this.baseUrl)
                .method("GET", null)
                .headers(this.headers)
                .build();
        Response response = client.newCall(request).execute();

        this.response = response.body().string();
        response.body().close();

        return this.response;
    }

    @Override
    public String jsonPost(PostBody postBody) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        String postData = mapper.writeValueAsString(postBody);
        this.requestBody = RequestBody.create(postData, JSON);
        OkHttpClient client = getUnsafeOkHttpClient();
        Request request = new Request.Builder()
                .url(this.baseUrl)
                .post(this.requestBody)
                .headers(this.headers)
                .build();
        Response response = client.newCall(request).execute();

        this.response = response.body().string();

        response.body().close();

        return this.response;
    }

    public String getStringRequestBody() {
        return "{\"url\":\"" + this.baseUrl + "\", \"body\":" + bodyToString(this.requestBody) + "}";
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
