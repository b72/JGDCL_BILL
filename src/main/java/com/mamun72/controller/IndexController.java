package com.mamun72.controller;

import com.mamun72.billarApi.JgdlApi;
import com.mamun72.entity.Bill;
import com.mamun72.repo.TestRepo;
import com.mamun72.service.BillPayService;
import com.mamun72.service.TestTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    TestTableService testTableService;
    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    public String index(Model model) {
        for (Object obj : testTableService.getAll()) {System.out.println(obj.toString());}
        model.addAttribute("tests", testTableService.getAll());
        return "test";
    }

    @Autowired
    BillPayService billPayService;
    @RequestMapping(value = "/bill", method = RequestMethod.GET)
    public String bill(Model model) {
        for (Object obj : billPayService.getAllBills()) {System.out.println(obj.toString());}
        model.addAttribute("bills", billPayService.getAllBills());
        return "index";
    }

    @RequestMapping(value = "/addBill", method = RequestMethod.GET)
    public String addBill(Bill bill) {
        return "addBill";
    }

    @RequestMapping(value = "/saveBill", method = RequestMethod.POST)
    public String saveBill() {
        return "index";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public @ResponseBody String login(
            @RequestParam("userName") String userName,
            @RequestParam("BranchCodeint") String BranchCodeint,
            @RequestParam("brName") String brName,
                                      Model model) throws Exception {

        JgdlApi jgdlApi = new JgdlApi();
        String res = jgdlApi.getBillInfo("110105718");
        return userName + BranchCodeint + brName+res;
    }

    @RequestMapping(value="/getCustomerById",
            method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<String> login(
            @RequestParam("customerId") String customerId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        headers.add(HttpHeaders.CONNECTION, "close");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        try {
            JgdlApi jgdlApi = new JgdlApi();
            String res = jgdlApi.getBillInfo(customerId);
            return ResponseEntity.ok().headers(headers).body(res);
        }
        catch (Exception e){
            return  ResponseEntity.badRequest().headers(headers).body(e.getMessage());
        }

    }
}
