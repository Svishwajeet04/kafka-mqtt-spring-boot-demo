package com.example.demo.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
@Slf4j
public class MyFilter extends OncePerRequestFilter {

    @Value("#{}")
    private String username;

    @Value("#{}")
    private String password;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String authToken = request.getHeader("Authorization");
            authToken = authToken.replace("Basic ", "");
            String[] creds = new String(Base64.getDecoder().decode(authToken.getBytes())).split(":");
            String username = creds[0];
            String password = creds[1];
            if (username.equalsIgnoreCase(this.username) && password.equalsIgnoreCase(this.password)) {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(UsernamePasswordAuthenticationToken.authenticated(username, password,
                                AuthorityUtils.createAuthorityList("ADMIN")));
            }
        } catch (Exception e) {
            log.error("error occurred {}", e.getMessage(), e);
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
