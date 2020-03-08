package com.mamun72.controller;

import com.mamun72.entity.User;
import com.mamun72.repo.ApiLogRepo;
import com.mamun72.service.BillPayService;
import com.mamun72.utils.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
public class DashboardController {

    @Autowired
    BillPayService billPayService;
    @Autowired
    ApiLogRepo apiLogRepo;
    @Autowired
    Session session;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dashboard(Model model) {
        User user = session.getLoggedInUser();

        if (user != null) {
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
            User user = session.getLoggedInUser();
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
        User user = session.getLoggedInUser();

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            return "getbill";
        }
        return "error";
    }

    @RequestMapping(value = "/get-report", method = RequestMethod.GET)
    public String getReport(Model model) {
        User user = session.getLoggedInUser();

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            return "report";
        }
        return "error";
    }

    @RequestMapping(value = "/get-report/local", method = RequestMethod.GET)
    public String getReportLocal(Model model) {
        User user = session.getLoggedInUser();

        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            return "report2";
        }
        return "error";
    }
}








