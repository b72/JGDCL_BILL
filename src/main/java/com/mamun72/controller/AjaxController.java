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
import com.mamun72.dto.BranchWiseCollectionReport;
import com.mamun72.service.BillPayService;
import com.mamun72.utils.Logger;
import com.mamun72.utils.Session;
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

    @Autowired
    BillPayService billPayService;
    @Autowired
    Logger logger;
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
        Integer status = 200;
        String response = null;
        if (session.getLoggedInUser() == null) {

            return ResponseEntity.status(403).headers(sentHeader()).body("Session timeout!");
        }
        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            JgdlApi jgdlApi = new JgdlApi();

            try {
                String res = jgdlApi.getBillInformation(customerId);
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                BillResponse billResponse = mapper.readValue(res, BillResponse.class);
                if (billResponse.getStatus() == JgdlConfig.getApiSuccessCode()) {
                    billResponse.setStampCharge((billResponse.getPaybleAmount() >= 400) ? JgdlConfig.getStampCharge() : 0.00);
                    Bill newBill = saveBill(billResponse);
                    billResponse.setTransactionId(newBill.getTransactionId());
                    String json = mapper.writeValueAsString(billResponse);
                    status = 200;
                    response = json;
                } else {
                    status = 404;
                    response = res;
                }
            } catch (Exception e) {
                status = 500;
                response = e.getMessage();
            }
            ApiLog apiLog = new ApiLog();
            apiLog.setLogId(customerId);
            apiLog.setResponse(response);
            apiLog.setRequest(jgdlApi.getStringRequestBody());
            logger.keepApiLog(apiLog);
        } else {
            status = 401;
            response = "X-CSRF-TOKEN not found or mismatch";
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
        Integer status = 200;
        String response = null;
        User logged = session.getLoggedInUser();
        if (logged == null) {
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

                String res = jgdlApi.payBill(payBillRequest);
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
                    response = res;
                } else {
                    status = 400;
                    billPayService.payBill(
                            payBillRequest,
                            payBillResponse.getTransactionId(),
                            JgdlConfig.getUnPaidStatus(),
                            logged
                    );
                    response = res;
                }

            } catch (Exception e) {

                response = e.getMessage();
                status = 500;
            }
            ApiLog apiLog = new ApiLog();
            apiLog.setLogId(trxId);
            apiLog.setResponse(response);
            apiLog.setRequest(jgdlApi.getStringRequestBody());
            logger.keepApiLog(apiLog);
        } else {
            status = 401;
            response = "X-CSRF-TOKEN not found or mismatch";
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
        Integer status = 200;
        String response = null;

        List<BillReport> paidBills;

        if (session.getLoggedInUser() == null) {

            return ResponseEntity.status(403).headers(sentHeader()).body("Session timeout!");
        }

        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            JgdlApi jgdlApi = new JgdlApi();

            try {
                status = 200;
                ObjectMapper objectMapper = new ObjectMapper();

                    if (reportType == 1) {
                        String res = jgdlApi.getReport(fromDate, toDate);
                        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
                        try {
                            paidBills = Arrays.asList(objectMapper.readValue(res, BillReport[].class));
                            List<String> iDList = paidBills.stream().map((billReport) -> billReport.getTransactionId()).collect(Collectors.toList());
                            System.out.println(iDList.toString());
                        } catch (Exception e) {
                            Error reportError = objectMapper.readValue(res, Error.class);
                            throw new Exception(reportError.getMessage(), e);
                        }
                        return ResponseEntity.status(status).headers(sentHeader()).body(res);
                    } else {
                        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                        List bills = billPayService.getBillByBranchStatus(session.getLoggedInUser().getBrCode(), JgdlConfig.getUnPaidStatus());
                        return ResponseEntity.status(status).headers(sentHeader()).body(objectMapper.writeValueAsString(bills));
                    }

            } catch (Exception e) {
                status = 500;
                response = e.getMessage();
            }
        } else {
            status = 401;
            response = "X-CSRF-TOKEN not found or mismatch";
        }

        return ResponseEntity.status(status).headers(sentHeader()).body(response);
    }


    @RequestMapping(value = "/ajax/getReport/branch", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<String> getBranchWiseReport(
            @RequestParam("fromDate") String fromDate,
            @RequestParam("toDate") String toDate,
            @RequestHeader(name = "X-CSRF-TOKEN") String csrf_token,
            HttpServletRequest request
    ) {
        int status = 200;
        String response = null;
        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            if (session.getLoggedInUser() == null) {
                return ResponseEntity.status(403).headers(sentHeader()).body("Session timeout!");
            }
            List bills = null;
            try {

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                User loggedUser = session.getLoggedInUser();
                bills = billPayService.getBillByBranchStatus(loggedUser.getUserId(), JgdlConfig.getUnPaidStatus());
            } catch (Exception e) {
                status = 500;
                response = e.getMessage();
            }
        } else {
            status = 401;
            response = "X-CSRF-TOKEN not found or mismatch";
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
        return saved;
    }

}
