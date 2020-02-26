package com.mamun72.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamun72.entity.User;
import com.mamun72.service.BillPayService;
import com.mamun72.service.UserService;
import com.mamun72.utils.Decryptor;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Controller
public class IndexController {

    @Autowired
    UserService userService;


    @Autowired
    BillPayService billPayService;


    @RequestMapping(value = "/userlogin", method = RequestMethod.GET)
    public
    void login(
            @RequestParam HashMap<String, String> reqParam,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        reqParam.forEach((key,value)-> reqParam.replace(key, new Decryptor().decrypt(value).trim()));
        String json = mapper.writeValueAsString(reqParam);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        User us = mapper.readValue(json, User.class);
         Optional<User> user = userService.getOneByName(us.getUserName());
        if(user.isPresent()){
            User found = user.get();
            if(createAuth(found)) {
                /*
                * redirect user to dashboard
                * */
                //httpServletResponse.sendRedirect("/dashboard");
                httpServletResponse.sendRedirect("/get-bill");

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
            User created = userService.saveUser(us);
            if((created.getUserName() != null) && createAuth(created)) {
                /*
                * User created & logged in
                * redirect user to dashboard
                *
                * */
                httpServletResponse.sendRedirect("/get-bill");
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
    @Nullable
    private boolean createAuth(User user){
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.createAuthorityList(user.getUserType()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Authentication authenticationUser = SecurityContextHolder.getContext().getAuthentication();
        if (!(authenticationUser instanceof AnonymousAuthenticationToken)) {
            return true;
        }else{
            return false;
        }
    }
}
