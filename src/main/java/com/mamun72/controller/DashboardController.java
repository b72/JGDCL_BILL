package com.mamun72.controller;

import com.mamun72.billarApi.JgdlApi;
import com.mamun72.billarApi.PayBill;
import com.mamun72.entity.ApiLog;
import com.mamun72.entity.User;
import com.mamun72.service.ApiLogService;
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
import java.io.IOException;
import java.util.Map;


@Controller
public class DashboardController {

    @Autowired
    ApiLogService apiLogService;

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
        User user = getLoggedInUser();
        if (user == null) {
            response.sendRedirect("/errorPage?code=404");
        } else {
            request.logout();
            response.sendRedirect("/errorPage?code=4040");
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
        if (csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())) {
            JgdlApi jgdlApi = new JgdlApi();
            jgdlApi.setActiveLog(false);
            try {
                String res = jgdlApi.getBillInfo(customerId);

                ApiLog apiLog = new ApiLog();
                apiLog.setLogId(customerId);
                apiLog.setRequest(jgdlApi.getFinalUrl());
                apiLog.setResponse(res);
                keepApiLog(apiLog);

                JsonParser springParser = JsonParserFactory.getJsonParser();
                Map<String, Object> map = springParser.parseMap(res);


                if ((int) map.get("status") == 200) {
                    // process request & do other stuffs
                    status = 200;
                    response = res;
                } else {
                    status = 404;
                    response = res;
                }
            } catch (Exception e) {
                ApiLog apiLog = new ApiLog();
                apiLog.setLogId(customerId);
                apiLog.setRequest(jgdlApi.getFinalUrl());
                apiLog.setResponse(e.getMessage());
                keepApiLog(apiLog);

                status = 500;
                response = e.getMessage();
            }
        } else {
            status = 405;
            response = "X-CSRF-TOKEN not found or mismatch";
        }
        return ResponseEntity.status(status).headers(sentHeaders).body(response);
    }


    /*
     * Ajax call for pay bill in JGDCL server
     * param json body
     * */
    @RequestMapping(value = "/ajax/payment",
            method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<String> payBill(@RequestBody Map<String, Object> payload) {
        Integer status = 200;
        String response = null;
        HttpHeaders sentHeaders = new HttpHeaders();
        sentHeaders.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        sentHeaders.add(HttpHeaders.CONNECTION, "close");
        sentHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        JgdlApi jgdlApi = new JgdlApi();
        jgdlApi.setApiLogService(apiLogService);
        jgdlApi.setActiveLog(true);
        PayBill bill = new PayBill();
        bill.setCustomerId(payload.get("customerId").toString());
        bill.setPaidAmount(Double.parseDouble(payload.get("paidAmount").toString()));
        bill.setBankName(payload.get("bankName").toString());
        bill.setTransactionId(payload.get("transactionId").toString());
        bill.setMobileNo(payload.get("mobileNo").toString());
        try {
            String res = jgdlApi.payBill(bill);
            JsonParser springParser = JsonParserFactory.getJsonParser();
            Map<String, Object> map = springParser.parseMap(res);
            response = res;
            status = 200;
        } catch (Exception e) {
            response = e.getMessage();
            status = 500;
        }
        return ResponseEntity.status(status).headers(sentHeaders).body(response);
    }


    private User getLoggedInUser() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user;
        } catch (Exception ex) {
            return null;
        }
    }

    private void keepApiLog(ApiLog apiLog) {
        apiLogService.saveLog(apiLog);
        System.out.println(apiLog.toString());
        System.out.println("SAVE CALL FROM CONTROLLER");
    }


}
