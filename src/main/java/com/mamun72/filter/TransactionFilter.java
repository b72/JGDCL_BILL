package com.mamun72.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
@Order(1)
public class TransactionFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        /*LOG.info(
                "Starting a transaction for req : {}",
                req.getRequestURI());*/

        filterChain.doFilter(servletRequest, servletResponse);
       /* LOG.info(
                "Committing a transaction for req : {}",
                req.getRequestURI());*/

    }

}
