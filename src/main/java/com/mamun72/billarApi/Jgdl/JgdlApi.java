package com.mamun72.billarApi.Jgdl;


import com.mamun72.billarApi.Api;
import com.mamun72.billarApi.Jgdl.POJO.PayBillRequest;
import okhttp3.Credentials;
import okhttp3.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class JgdlApi extends Api {

    Logger logger = LoggerFactory.getLogger(JgdlApi.class);

    public JgdlApi() {
        super();
    }


    public String getBillInformation(String customerId) throws IOException {

        setApi(JgdlConfig.getGetCustomer());
        HashMap<String, String> queryParam = new HashMap<>();
        queryParam.put("customerId", customerId);
        setLogId(customerId);
        return getRequest(queryParam);
    }

    public String payBill(PayBillRequest payBillRequest) {
        try {
            setApi(JgdlConfig.getPayment());
            payBillRequest.setBankName(JgdlConfig.getBankName());
            return jsonPost(payBillRequest);
        } catch (Exception e) {
            logger.info("error from pay bill api" + e.getMessage() +" - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
        }
        return null;
    }

    public String getReport(String fromDate, String toDate) throws IOException {
        setApi(JgdlConfig.getGetAllBills());
        HashMap<String, String> queryParam = new HashMap<>();
        queryParam.put("fromDate", fromDate);
        queryParam.put("toDate", toDate);
        setLogId(fromDate + "|" + toDate);
        return getRequest(queryParam);
    }

    private void setApi(String requestPoint) {
        setBaseUrl(JgdlConfig.getBaseUrl() + requestPoint);
        Headers.Builder headerBuilder = new Headers.Builder();
        headerBuilder.add("Authorization", Credentials.basic(JgdlConfig.getUserName(), JgdlConfig.getPassword()));
        setHeaders(headerBuilder.build());
    }
}
