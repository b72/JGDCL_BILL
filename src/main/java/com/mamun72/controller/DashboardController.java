package com.mamun72.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamun72.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
public class DashboardController {


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody
    String dashboard(){
        User user = getLoggedInUser();
        if(user != null)  return "Welcome " + user.getUserName() + " this is your dashboard";
        else return "Please login first";
    }

    @RequestMapping(value = "/user-logout", method = RequestMethod.GET)
    public @ResponseBody
    void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = getLoggedInUser();
        if(user != null)  {
            response.sendRedirect("/errorPage?code=404");
        }
        else {
            request.logout();
            response.sendRedirect("/errorPage?code=4040");
        }
    }



    private User getLoggedInUser(){
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return  user;
        }catch (Exception ex){
            return null;
        }
    }
}
