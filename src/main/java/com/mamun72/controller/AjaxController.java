package com.mamun72.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mamun72.billarApi.Jgdl.JgdlApi;
import com.mamun72.billarApi.Jgdl.JgdlConfig;
import com.mamun72.billarApi.Jgdl.POJO.BillReport;
import com.mamun72.billarApi.Jgdl.POJO.BillResponse;
import com.mamun72.billarApi.Jgdl.POJO.PayBillRequest;
import com.mamun72.billarApi.Jgdl.POJO.PayBillResponse;
import com.mamun72.entity.ApiLog;
import com.mamun72.entity.Bill;
import com.mamun72.entity.User;
import com.mamun72.service.BillPayService;
import com.mamun72.utils.ApiLogger;
import com.mamun72.utils.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class AjaxController {

    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    BillPayService billPayService;
    @Autowired
    ApiLogger apiLogger;
    @Autowired
    Session session;

    /*
     * Ajax call for get bill info from JGDCL server
     * param string customerId
     * */
    @RequestMapping(value = "/ajax/getCustomerById",
            method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<String> getCustomer(
            @RequestParam("customerId") String customerId,
            @RequestHeader(name = "X-CSRF-TOKEN") String csrf_token,
            HttpServletRequest request) {
        logger.info("User hits /ajax/getCustomerById url with customerId " + customerId );
        Integer status = 200;
        String response = null;
        if (session.getLoggedInUser() == null) {
            logger.warn("User hits /ajax/getCustomerById url with customerId " + customerId + " but session not found 403 sent");
            return ResponseEntity.status(403).headers(sentHeader()).body("Session timeout!");
        }
        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            JgdlApi jgdlApi = new JgdlApi();

            try {
                logger.info("Bill fetching for customerId " + customerId );
                String res = jgdlApi.getBillInformation(customerId);
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                BillResponse billResponse = mapper.readValue(res, BillResponse.class);
                if (billResponse.getStatus() == JgdlConfig.getApiSuccessCode()) {
                    logger.info("Bill fetched for customerId " + customerId );
                    billResponse.setStampCharge((billResponse.getPaybleAmount() >= 400) ? JgdlConfig.getStampCharge() : 0.00);
                    Bill newBill = saveBill(billResponse);
                    billResponse.setTransactionId(newBill.getTransactionId());

                    logger.info("Bill served for customerId " + customerId + " with systemTrxID " + billResponse.getTransactionId());

                    String json = mapper.writeValueAsString(billResponse);
                    status = 200;
                    response = json;
                } else {
                    logger.error("Bill fetched for customerId " + customerId + " with status code " + billResponse.getStatus() );
                    status = 404;
                    response = res;
                }
            } catch (Exception e) {
                logger.error("Exception occurred during fetching bill for customerId " + customerId + " error " + e.getMessage());
                status = 500;
                response = e.getMessage();
            }
            ApiLog apiLog = new ApiLog();
            apiLog.setLogId(customerId);
            apiLog.setResponse(response);
            apiLog.setRequest(jgdlApi.getStringRequestBody());
            apiLogger.keepApiLog(apiLog);
            logger.info("Api response logged for customerId " + customerId + " with logId " + apiLog.getLogId() );
        } else {
            status = 401;
            response = "X-CSRF-TOKEN not found or mismatch";
            logger.warn("User hits /ajax/getCustomerById url with customerId " + customerId + " but " + response + " not found " + status + " sent ");
        }

        return ResponseEntity.status(status).headers(sentHeader()).body(response);
    }


    /*
     * Ajax call for pay bill in JGDCL server
     * param json body
     * */
    @RequestMapping(value = "/ajax/payment",
            method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public @ResponseBody
    ResponseEntity<String> payBill(
            HttpServletRequest request,
            @RequestBody HashMap<String, String> payload,
            @RequestHeader(name = "X-CSRF-TOKEN") String csrf_token) {

        StringBuilder requestStr = new StringBuilder();
        payload.forEach((key, value) -> requestStr.append(key + " = " + value));
        logger.info("User hits /ajax/payment url with json " + requestStr.toString());
        Integer status = 200;
        String response = null;
        User logged = session.getLoggedInUser();
        if (logged == null) {
            logger.warn("User session not found 403 sent");
            return ResponseEntity.status(403).headers(sentHeader()).body("Session timeout!");
        }
        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            JgdlApi jgdlApi = new JgdlApi();
            String trxId = payload.get("transactionId").toString();
            try {
                PayBillRequest payBillRequest = new PayBillRequest();
                payBillRequest.setTransactionId(trxId);
                payBillRequest.setCustomerId(payload.get("customerId").toString());
                payBillRequest.setPaidAmount(Double.parseDouble(payload.get("paidAmount").toString()));
                payBillRequest.setMobileNo(payload.get("mobileNo").toString());
                logger.info("Bill pay api called with " + payBillRequest.toString() + " - invoked " + new Throwable()
                        .getStackTrace()[0]
                        .getMethodName());
                String res = jgdlApi.payBill(payBillRequest);
                logger.info("Bill pay api responded with " + res );
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                PayBillResponse payBillResponse = mapper.readValue(res, PayBillResponse.class);
                if (payBillResponse.getStatus() == JgdlConfig.getApiSuccessCode()) {
                    status = 200;
                    int update =
                            billPayService.payBill(
                                    payBillRequest,
                                    payBillResponse.getTransactionId(),
                                    JgdlConfig.getPaidStatus(),
                                    logged
                            );
                    logger.info("Bill paid with sysTrxId " + payBillRequest.getTransactionId() + " and apiTxId " + payBillResponse.getTransactionId() + " api message " + payBillResponse.getMessage() + " userId " + logged.getUserId());
                    response = res;
                } else {
                    status = 400;
                    billPayService.payBill(
                            payBillRequest,
                            payBillResponse.getTransactionId(),
                            JgdlConfig.getUnPaidStatus(),
                            logged
                    );
                    logger.warn("Bill unpaid with sysTrxId " + trxId + " and apiTxId " + payBillResponse.getTransactionId() + " api message " + payBillResponse.getMessage() + " userId " + logged.getUserId() + " - invoked " + new Throwable()
                            .getStackTrace()[0]
                            .getMethodName());
                    response = res;
                }

            } catch (Exception e) {

                response = e.getMessage();
                status = 500;
                logger.error("Exception occurred during paying bill for sysTrxId " + trxId + " error " + e.getMessage());
            }
            ApiLog apiLog = new ApiLog();
            apiLog.setLogId(trxId);
            apiLog.setResponse(response);
            apiLog.setRequest(jgdlApi.getStringRequestBody());
            apiLogger.keepApiLog(apiLog);
            logger.info("Api response logged for sysTrxId " + trxId + " with logId " + apiLog.getLogId());
        } else {
            status = 401;
            response = "X-CSRF-TOKEN not found or mismatch";
            logger.error(response + " " + status + " sent");
        }
        return ResponseEntity.status(status).headers(sentHeader()).body(response);
    }


    /*
     * Ajax call for get bill info from JGDCL server
     * param string customerId
     * */
    @RequestMapping(value = "/ajax/getReport",
            method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<String> getReport(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestParam("reportType") int reportType,
            @RequestHeader(name = "X-CSRF-TOKEN") String csrf_token,
            HttpServletRequest request) {
        logger.info("User hits /ajax/getReport with param fromDate " + fromDate + " toDate " + toDate + " reportType " + reportType);

        Integer status = 200;
        String response = null;

        List<BillReport> paidBills;

        if (session.getLoggedInUser() == null) {
            logger.warn("User session not found 403 sent");
            return ResponseEntity.status(403).headers(sentHeader()).body("Session timeout!");
        }

        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            JgdlApi jgdlApi = new JgdlApi();

            try {
                status = 200;
                ObjectMapper objectMapper = new ObjectMapper();

                if (reportType == 1) {
                    logger.info(" Report API called with fromDate " + fromDate + " toDate " + toDate );
                    String res = jgdlApi.getReport(fromDate, toDate);
                    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                    try {
                        paidBills = Arrays.asList(objectMapper.readValue(res, BillReport[].class));
                        List<String> iDList = paidBills.stream().map((billReport) -> billReport.getTransactionId()).collect(Collectors.toList());
                        //System.out.println(iDList.toString());
                        logger.info(" Report API responded with "+iDList.size()+" number of bill fromDate " + fromDate + " toDate " + toDate );
                    } catch (Exception e) {
                        Error reportError = objectMapper.readValue(res, Error.class);
                        throw new Exception(reportError.getMessage(), e);
                    }
                    return ResponseEntity.status(status).headers(sentHeader()).body(res);
                } else {
                    logger.info("Database query with fromDate " + fromDate + " toDate " + toDate + " status " + request.getParameter("billstatus"));
                    List bills = null;
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

                    if(Integer.parseInt(request.getParameter("billstatus")) != 0) {
                        System.out.println("Bill status " + Integer.parseInt(request.getParameter("billstatus")));
                         bills = billPayService.getBillByBranchStatus(fromDate, toDate, session.getLoggedInUser().getBrCode(), Integer.parseInt(request.getParameter("billstatus")));
                    }
                    else
                         bills = billPayService.getBillByBranchStatus(fromDate, toDate, session.getLoggedInUser().getBrCode());

                    return ResponseEntity.status(status).headers(sentHeader()).body(objectMapper.writeValueAsString(bills));
                }

            } catch (Exception e) {
                status = 500;
                response = e.getMessage();
                logger.error("Exception occurred during getting report fromDate" + fromDate + " toDate " + toDate +  " error " + response );
            }
        } else {
            status = 401;
            response = "X-CSRF-TOKEN not found or mismatch";
            logger.error(response + " " + status + " sent");
        }

        return ResponseEntity.status(status).headers(sentHeader()).body(response);
    }


    @RequestMapping(value = "/ajax/getReport/branch", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<String> getBranchWiseReport(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            //@RequestHeader(name = "X-CSRF-TOKEN") String csrf_token,
            HttpServletRequest request
    ) {
        int status = 200;
        String response = null;

            List bills = null;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                bills = billPayService.getBillByBranchStatus(fromDate, toDate, "1914");
                response = objectMapper.writeValueAsString(bills);
            } catch (Exception e) {
                status = 500;
                response = e.getMessage();
            }
        return ResponseEntity.status(status).headers(sentHeader()).body(response);
    }

    private HttpHeaders sentHeader() {
        HttpHeaders sentHeaders = new HttpHeaders();
        sentHeaders.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        sentHeaders.add(HttpHeaders.CONNECTION, "close");
        sentHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return sentHeaders;
    }

    /*
     * Save bill
     * return Bill that saved
     * */
    private Bill saveBill(BillResponse billResponse) {
        User user = session.getLoggedInUser();
        Bill bill = new Bill();
        bill.setCustomerId(billResponse.getCustomerId());
        bill.setCustomerName(billResponse.getCustomerName());
        bill.setMonYear(billResponse.getMonyear());
        bill.setBillAmount(billResponse.getBillAmount());
        bill.setPaybleAmount(billResponse.getPaybleAmount());
        bill.setBillcount(billResponse.getBillcount());
        bill.setPaidAmount(0);
        bill.setMobileNo(null);
        bill.setStatus(JgdlConfig.getUnPaidStatus());
        bill.setBankName(JgdlConfig.getBankName());
        bill.setSurcharge(billResponse.getSurcharge());
        bill.setStampCharge(billResponse.getStampCharge());
        bill.setUser(user);
        Bill saved = billPayService.saveBill(bill);
        logger.info("Bill Saved for customerId " + saved.getCustomerId());
        return saved;
    }

}
