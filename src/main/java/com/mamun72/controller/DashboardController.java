package com.mamun72.controller;

import com.mamun72.billarApi.JgdlApi;
import com.mamun72.entity.User;
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


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String dashboard(Model model){
        User user = getLoggedInUser();

        if(user != null)  {
            System.out.println(user);
            model.addAttribute("user", user);
            model.addAttribute("title", "JGDCL|NBL");
            model.addAttribute("name", "Jalalabd Gas Distribution Company Limited");
            return "index";
        }
        else return "index";
    }

    @RequestMapping(value = "/user-logout", method = RequestMethod.GET)
    public @ResponseBody
    void logout(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = getLoggedInUser();
        if(user == null)  {
            response.sendRedirect("/errorPage?code=404");
        }
        else {
            request.logout();
            response.sendRedirect("/errorPage?code=4040");
        }
    }
    @RequestMapping(value = "/get-bill", method = RequestMethod.GET)
    public String getBill(Model model){
        User user = getLoggedInUser();

        if(user != null)  {
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
        HttpHeaders sentHeaders = new HttpHeaders();
        sentHeaders.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        sentHeaders.add(HttpHeaders.CONNECTION, "close");
        sentHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        if(csrf_token.equals(new HttpSessionCsrfTokenRepository().loadToken(request).getToken())){
            try {
                JgdlApi jgdlApi = new JgdlApi();
                String res = jgdlApi.getBillInfo(customerId);
                JsonParser springParser = JsonParserFactory.getJsonParser();
                Map<String, Object> map = springParser.parseMap(res);
                if ((int) map.get("status") == 200) {
                    // process request & do other stuffs
                    return ResponseEntity.ok().headers(sentHeaders).body(res);
                } else
                    return ResponseEntity.badRequest().headers(sentHeaders).body(res);
            } catch (Exception e) {
                return ResponseEntity.status(500).headers(sentHeaders).body(e.getMessage());
            }
        }else{
            return ResponseEntity.status(400).headers(sentHeaders).body("X-CSRF-TOKEN not found or mismatch");
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
