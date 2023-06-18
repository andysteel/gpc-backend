package com.gmail.andersoninfonet.gpc.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class RequisicaoFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        var origin = httpRequest.getHeader("Origin");
        if (servletRequest.isSecure() && origin == null) {
            throw new AccessDeniedException("Requisição invalida");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
