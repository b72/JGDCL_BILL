package com.mamun72.controller;

import com.mamun72.entity.Bill;
import com.mamun72.repo.TestRepo;
import com.mamun72.service.BillPayService;
import com.mamun72.service.TestTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
}
