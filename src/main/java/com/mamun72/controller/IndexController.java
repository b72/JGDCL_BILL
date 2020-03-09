package com.mamun72.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mamun72.entity.User;
import com.mamun72.service.BillPayService;
import com.mamun72.service.UserService;
import com.mamun72.utils.Decryptor;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(IndexController.class);

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
        logger.info("User hits /userlogin url - invoked " + new Throwable()
                .getStackTrace()[0]
                .getMethodName());
        ObjectMapper mapper = new ObjectMapper();
        reqParam.forEach((key,value)-> reqParam.replace(key, new Decryptor().decrypt(value).trim()));
        String json = mapper.writeValueAsString(reqParam);
        logger.info("User hits /userlogin url with this params " +json + "- invoked " + new Throwable()
                .getStackTrace()[0]
                .getMethodName());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        User us = mapper.readValue(json, User.class);
         Optional<User> user = userService.getOneByName(us.getUserName());
        if(user.isPresent()){
            logger.info("User hits /userlogin url existing userId " + us.getUserId() + " - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            User found = user.get();
            if(createAuth(found)) {
                logger.info("User userId " + us.getUserId() + " logged and redirected to /get-bill - invoked " + new Throwable()
                        .getStackTrace()[0]
                        .getMethodName());
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
                logger.error("User userId " + us.getUserId() + " but unable to logg and redirected to /error-page with code 404 - invoked " + new Throwable()
                        .getStackTrace()[0]
                        .getMethodName());
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
                logger.info("User userId " + us.getUserId() + " created & logged and redirected to /get-bill - invoked " + new Throwable()
                        .getStackTrace()[0]
                        .getMethodName());
                httpServletResponse.sendRedirect("/get-bill");
            }
            else if((created.getUserName() != null) && !createAuth(created)) {
                /*
                 * User created & But not logged in
                 * redirect user to error
                 *
                 * */
                logger.error("User userId " + us.getUserId() + " created & but unable to log and redirected to /errorPage with code 500 - invoked " + new Throwable()
                        .getStackTrace()[0]
                        .getMethodName());
                httpServletResponse.sendRedirect("/errorPage?code=500");
            }
            else {
                /*
                 * Something went wrong
                 * redirect user to error
                 *
                 * */
                logger.error("User userId " + us.getUserId() + " unable to create & unable to log and redirected to /errorPage with code \"\" - invoked " + new Throwable()
                        .getStackTrace()[0]
                        .getMethodName());
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
            logger.info("User userId " + user.getUserId() + " logged - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            return true;
        }else{
            logger.info("User userId " + user.getUserId() + " unable to logged - invoked " + new Throwable()
                    .getStackTrace()[0]
                    .getMethodName());
            return false;
        }
    }
}
