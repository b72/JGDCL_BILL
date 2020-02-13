package com.mamun72.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamun72.entity.Bill;
import com.mamun72.entity.User;
import com.mamun72.service.BillPayService;
import com.mamun72.service.UserService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    UserService userService;


    @Autowired
    BillPayService billPayService;

    @RequestMapping(value = "/bill", method = RequestMethod.GET)
    public String bill(Model model) {
        for (Object obj : billPayService.getAllBills()) {
            System.out.println(obj.toString());
        }
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

    @RequestMapping(value = "/userlogin", method = RequestMethod.GET)
    public
    void login(
            @RequestParam Map<String, String> reqParam,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        final User us = mapper.convertValue(reqParam, User.class);
         Optional<User> user = userService.getOneByName(us.getUserName());
        if(user.isPresent()){
            User found = user.get();
            if(createAuth(found)) {
                /*
                * redirect user to dashboard
                * */
                //httpServletResponse.sendRedirect("/dashboard");
                httpServletResponse.sendRedirect("/");

            }
            else {
                /*
                * user not found or unable to login
                * redirect user to error
                * */
                httpServletResponse.sendRedirect("/errorPage?code=404");
            }
        }
        else{
            User userNw = new User();
            userNw.setUserName(us.getUserName());
            userNw.setBranchCodeint(us.getBranchCodeint());
            userNw.setBrName(us.getBrName());
            userNw.setUserId(us.getUserId());
            User created = userService.saveUser(userNw);
            if((created.getUserName() != null) && createAuth(created)) {
                /*
                * User created & logged in
                * redirect user to dashboard
                *
                * */
                httpServletResponse.sendRedirect("/");
            }
            else if((created.getUserName() != null) && !createAuth(created)) {
                /*
                 * User created & But not logged in
                 * redirect user to error
                 *
                 * */
                httpServletResponse.sendRedirect("/errorPage?code=500");
            }
            else {
                /*
                 * Something went wrong
                 * redirect user to error
                 *
                 * */
                httpServletResponse.sendRedirect("/errorPage?code=");
            }
        }
    }

/*    @RequestMapping(value = "/ajax/getCustomerById",
            method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<String> login(
            @RequestParam("customerId") String customerId, HttpSession httpSession) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        headers.add(HttpHeaders.CONNECTION, "close");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        System.out.println(httpSession.getAttribute("USER").toString());
        try {
            JgdlApi jgdlApi = new JgdlApi();
            String res = jgdlApi.getBillInfo(customerId);
            JsonParser springParser = JsonParserFactory.getJsonParser();
            Map<String, Object> map = springParser.parseMap(res);
            if (map.get("status") == "200") {
                // process request & do other stuffs
                return ResponseEntity.ok().headers(headers).body(res);
            } else
                return ResponseEntity.badRequest().headers(headers).body(res);
        } catch (Exception e) {
            return ResponseEntity.status(500).headers(headers).body(e.getMessage());
        }
    }*/

    @Nullable
    private boolean createAuth(User user){
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Authentication authenticationUser = SecurityContextHolder.getContext().getAuthentication();
        if (!(authenticationUser instanceof AnonymousAuthenticationToken)) {
            return true;
        }else{
            return false;
        }
    }
}
