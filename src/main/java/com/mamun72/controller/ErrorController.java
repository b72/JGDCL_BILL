package com.mamun72.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {
    @RequestMapping(value = "/errorPage")
    public ModelAndView renderErrorPage(  @RequestParam("code") Integer errorCode) {
        ModelAndView errorPage = new ModelAndView("errorPage");
        String errorMsg = "";
        String path = "";
        int httpErrorCode = errorCode;

        switch (httpErrorCode) {
            case 400: {
                errorMsg = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorMsg = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                errorMsg = "Http Error Code: 404. Resource not found";
                break;
            }
            case 4040: {
                errorMsg = "Successfully logged out";
                break;
            }
            case 500: {
                errorMsg = "Http Error Code: 500. Internal Server Error";
                break;
            }
            case 5005: {
                errorMsg = "Unable to login please try again";
                break;
            }
            default:
                errorMsg = "Oppss!! Something went wrong";
                errorCode = null;
                path = "/";
        }
        errorPage.addObject("errorMsg", errorMsg);
        errorPage.addObject("code", errorCode);
        errorPage.addObject("path", path);
        return errorPage;
    }
}
