package com.mamun72.controller;

import com.mamun72.billarApi.JgdlApi;
import com.mamun72.entity.Bill;
import com.mamun72.entity.User;
import com.mamun72.service.BillPayService;
import com.mamun72.service.TestTableService;
import com.mamun72.service.UserService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    TestTableService testTableService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    public String index(Model model) {
        for (Object obj : testTableService.getAll()) {
            System.out.println(obj.toString());
        }
        model.addAttribute("tests", testTableService.getAll());
        return "test";
    }

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
    public @ResponseBody
    String login(
            @RequestParam("userName") String userName,
            @RequestParam("BranchCodeint") String BranchCodeint,
            @RequestParam("brName") String brName,
            @RequestParam("userId") String userId,
            HttpSession httpSession
    ) {
        String returnMsg = null;
         Optional<User> user = userService.getOneByName(userName);
        if(user.isPresent()){
            User found = user.get();
            if(createAuth(found)) returnMsg = found.toString();
            else returnMsg = "Unable to login";
        }
        else{
            User userNw = new User();
            userNw.setUserName(userName);
            userNw.setBranchCodeint(BranchCodeint);
            userNw.setBrName(brName);
            userNw.setUserId(userId);
            User created = userService.saveUser(userNw);
            if((created.getUserName() != null) && createAuth(created)) returnMsg = created.toString();
            else if((created.getUserName() != null) && !createAuth(created)) returnMsg = "Created but unable to auth";
            else returnMsg = "Something went wrong!";
        }
        return returnMsg;
    }

    @RequestMapping(value = "/getCustomerById",
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
    }

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
