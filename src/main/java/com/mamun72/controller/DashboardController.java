package com.mamun72.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mamun72.billarApi.Jgdl.JgdlApi;
import com.mamun72.billarApi.Jgdl.JgdlConfig;
import com.mamun72.billarApi.Jgdl.POJO.PayBillRequest;
import com.mamun72.billarApi.Jgdl.POJO.ReportError;
import com.mamun72.entity.ApiLog;
import com.mamun72.entity.Bill;
import com.mamun72.entity.User;
import com.mamun72.repo.ApiLogRepo;
import com.mamun72.service.BillPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@Controller
public class DashboardController {

    @Autowired
    BillPayService billPayService;
    @Autowired
    ApiLogRepo apiLogRepo;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dashboard(Model model) {
        User user = getLoggedInUser();

        if (user != null) {
            System.out.println(user);
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            return "index";
        } else return "index";
    }

    @RequestMapping(value = "/user-logout", method = RequestMethod.GET)
    public @ResponseBody
    void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            User user = getLoggedInUser();
            if (user == null) {
                response.sendRedirect("/errorPage?code=404");
            } else {
                HttpSession session = request.getSession(false);
                session.invalidate();
                response.sendRedirect("/errorPage?code=4040");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    @RequestMapping(value = "/get-bill", method = RequestMethod.GET)
    public String getBill(Model model) {
        User user = getLoggedInUser();

        if (user != null) {
            System.out.println(user);
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            return "getbill";
        }
        return "error";
    }

    @RequestMapping(value = "/get-report", method = RequestMethod.GET)
    public String getReport(Model model) {
        User user = getLoggedInUser();

        if (user != null) {
            System.out.println(user);
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            return "report";
        }
        return "error";
    }

    /*
     * Save bill
     * return Bill that saved
     * */
    private Bill saveBill(Map<String, Object> map) {
        Bill bill = new Bill();
        bill.setCustomerId(map.get("customerId").toString());
        bill.setCustomerName(map.get("customerName").toString());
        bill.setMonYear(map.get("monyear").toString());
        bill.setBillAmount(Double.parseDouble(map.get("billAmount").toString()));
        bill.setPaybleAmount(Double.parseDouble(map.get("paybleAmount").toString()));
        bill.setBillcount(Integer.parseInt(map.get("billcount").toString()));
        bill.setPaidAmount(0);
        bill.setMobileNo(null);
        bill.setStatus(JgdlConfig.getUnPaidStatus());
        bill.setBankName(JgdlConfig.getBankName());
        bill.setSurcharge(Double.parseDouble(map.get("surcharge").toString()));
        Bill saved = billPayService.saveBill(bill);
        return saved;
    }

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
        if (getLoggedInUser() == null) {

            return ResponseEntity.status(403).headers(sentHeader()).body("Session timeout!");
        }
        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            JgdlApi jgdlApi = new JgdlApi();

            try {
                String res = jgdlApi.getBillInformation(customerId);
                JsonParser springParser = JsonParserFactory.getJsonParser();
                Map<String, Object> map = springParser.parseMap(res);
                if ((int) map.get("status") == 200) {
                    String trxId = saveBill(map).getTransactionId();
                    map.put("transactionId", trxId);
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(map);
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
            keepApiLog(apiLog);
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
            @RequestBody Map<String, Object> payload,
            @RequestHeader(name = "X-CSRF-TOKEN") String csrf_token) {
        Integer status = 200;
        String response = null;
        User logged = getLoggedInUser();
        System.out.println(logged.getBranchCodeint());
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
                JsonParser springParser = JsonParserFactory.getJsonParser();
                Map<String, Object> map = springParser.parseMap(res);
                if ((int) map.get("status") == 200) {
                    status = 200;
                    int update =
                            billPayService.payBill(
                                    payBillRequest,
                                    map.get("transactionId").toString(),
                                    JgdlConfig.getPaidStatus(),
                                    logged
                            );
                    System.out.println("Bill Paid : " + update);
                    response = res;
                } else {
                    status = 400;
                    billPayService.payBill(payBillRequest, map.get("transactionId").toString(), JgdlConfig.getUnPaidStatus(), logged);
                    System.out.println("Bill UnPaid ");
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
            keepApiLog(apiLog);
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
            @RequestHeader(name = "X-CSRF-TOKEN") String csrf_token,
            HttpServletRequest request) {
        Integer status = 200;
        String response = null;
        PayBillRequest[] paidBills;
        if (getLoggedInUser() == null) {

            return ResponseEntity.status(403).headers(sentHeader()).body("Session timeout!");
        }
        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            JgdlApi jgdlApi = new JgdlApi();

            try {
                String res = jgdlApi.getReport(fromDate, toDate);

                ObjectMapper objectMapper = new ObjectMapper();
                try{
                    paidBills = objectMapper.readValue(res, PayBillRequest[].class);
                    System.out.println(paidBills.length);
                    status = 200;
                    response = res;
                }catch (Exception e){
                    ReportError reportError = objectMapper.readValue(res, ReportError.class);
                    throw new Exception(reportError.getMessage(), e);
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


    private HttpHeaders sentHeader() {
        HttpHeaders sentHeaders = new HttpHeaders();
        sentHeaders.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        sentHeaders.add(HttpHeaders.CONNECTION, "close");
        sentHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return sentHeaders;
    }

    private User getLoggedInUser() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    private void keepApiLog(ApiLog apiLog) {
        apiLogRepo.save(apiLog);
    }

}
