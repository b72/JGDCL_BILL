package com.mamun72.controller;

import com.mamun72.entity.User;
import com.mamun72.repo.ApiLogRepo;
import com.mamun72.service.BillPayService;
import com.mamun72.utils.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    BillPayService billPayService;
    @Autowired
    ApiLogRepo apiLogRepo;
    @Autowired
    Session session;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dashboard(Model model) {
        logger.info("User hits root url - invoked " + new Throwable()
                .getStackTrace()[0]
                .getMethodName());
        User user = session.getLoggedInUser();

        if (user != null) {
            logger.info("User session found - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            logger.info("User served index.html page - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName() );
            return "index";
        } else {
            logger.error("User server error.html page as session not found - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            return "error";
        }
    }

    @RequestMapping(value = "/user-logout", method = RequestMethod.GET)
    public @ResponseBody
    void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            logger.info("User hits /user-logout url - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            User user = session.getLoggedInUser();
            if (user == null) {
                logger.warn("User session not found redirected to error page with code 404 - invoked " + new Throwable()
                        .getStackTrace()[0]
                        .getMethodName());
                response.sendRedirect("/errorPage?code=404");
            } else {
                HttpSession session = request.getSession(false);
                session.invalidate();
                logger.info("User session found and destroyed. redirected to error page with code 4040 - invoked " + new Throwable()
                        .getStackTrace()[0]
                        .getMethodName());
                response.sendRedirect("/errorPage?code=4040");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            logger.error("Exception " + e.getMessage() + ". redirected to error page with code 500 - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            response.sendRedirect("/errorPage?code=500");
        }

    }


    @RequestMapping(value = "/get-bill", method = RequestMethod.GET)
    public String getBill(Model model) {
        logger.info("User hits /get-bill url - invoked " + new Throwable()
                .getStackTrace()[0]
                .getMethodName());
        User user = session.getLoggedInUser();

        if (user != null) {
            logger.info("User hits /get-bill url session found getbill page served - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            return "getbill";
        }
        logger.error("User hits /get-bill url session not found error page served - invoked " + new Throwable()
                .getStackTrace()[0]
                .getMethodName());
        return "error";
    }

    @RequestMapping(value = "/get-report", method = RequestMethod.GET)
    public String getReport(Model model) {
        logger.info("User hits /get-report url - invoked " + new Throwable()
                .getStackTrace()[0]
                .getMethodName());
        User user = session.getLoggedInUser();

        if (user != null) {
            logger.info("User hits /get-report url session found report page served - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            return "report";
        }
        logger.error("User hits /get-report url session not found error page served - invoked " + new Throwable()
                .getStackTrace()[0]
                .getMethodName());
        return "error";
    }

    @RequestMapping(value = "/get-report/local", method = RequestMethod.GET)
    public String getReportLocal(Model model) {
        logger.info("User hits /get-report/local url - invoked " + new Throwable()
                .getStackTrace()[0]
                .getMethodName());
        User user = session.getLoggedInUser();

        if (user != null) {
            logger.info("User hits /get-report/local url session found report2 page served - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            return "report2";
        }
        logger.error("User hits /get-report url session not found error page served - invoked " + new Throwable()
                .getStackTrace()[0]
                .getMethodName());
        return "error";
    }
}








