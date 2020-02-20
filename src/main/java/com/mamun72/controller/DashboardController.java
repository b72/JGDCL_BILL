package com.mamun72.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamun72.billarApi.JgdlApi;
import com.mamun72.billarApi.JgdlConfig;
import com.mamun72.billarApi.PayBillRequest;
import com.mamun72.entity.ApiLog;
import com.mamun72.entity.Bill;
import com.mamun72.entity.User;
import com.mamun72.service.ApiLogService;
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
import java.util.Map;


@Controller
public class DashboardController {

    @Autowired
    ApiLogService apiLogService;
    @Autowired
    BillPayService billPayService;

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
        HttpHeaders sentHeaders = new HttpHeaders();
        sentHeaders.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        sentHeaders.add(HttpHeaders.CONNECTION, "close");
        sentHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        if (getLoggedInUser() == null) {
            return ResponseEntity.status(403).headers(sentHeaders).body("Session timeout!");
        }
        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            JgdlApi jgdlApi = new JgdlApi();
            jgdlApi.setApiLogService(apiLogService);
            jgdlApi.setActiveLog(true);
            try {
                String res = jgdlApi.getBillInfo(customerId);
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
                System.out.println("getCustomer " + e.toString());
                ApiLog apiLog = new ApiLog();
                apiLog.setLogId(customerId);
                apiLog.setRequest(jgdlApi.getStringBody());
                apiLog.setResponse(e.getMessage());
                keepApiLog(apiLog);

                status = 500;
                response = e.getMessage();
            }
        } else {
            status = 401;
            response = "X-CSRF-TOKEN not found or mismatch";
        }
        return ResponseEntity.status(status).headers(sentHeaders).body(response);
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
        HttpHeaders sentHeaders = new HttpHeaders();
        sentHeaders.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        sentHeaders.add(HttpHeaders.CONNECTION, "close");
        sentHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        User logged = getLoggedInUser();
        System.out.println(logged.toString());
        if (logged == null) {
            return ResponseEntity.status(403).headers(sentHeaders).body("Session timeout!");
        }
        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            JgdlApi jgdlApi = new JgdlApi();
            jgdlApi.setApiLogService(apiLogService);
            jgdlApi.setActiveLog(true);
            try {
                String trxId = payload.get("transactionId").toString();
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
                    System.out.println("Bill Paid : "  + update);
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
                ApiLog apilog = new ApiLog();
                apilog.setLogId("1234");
                apilog.setRequest(jgdlApi.getStringBody());
                apilog.setResponse(e.getMessage());
                keepApiLog(apilog);
            }
        } else {
            status = 401;
            response = "X-CSRF-TOKEN not found or mismatch";
        }
        return ResponseEntity.status(status).headers(sentHeaders).body(response);
    }


    private User getLoggedInUser() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    private void keepApiLog(ApiLog apiLog) {
        apiLogService.saveLog(apiLog);
        System.out.println("SAVE CALL FROM CONTROLLER");
    }

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



}
