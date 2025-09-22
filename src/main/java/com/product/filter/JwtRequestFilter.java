package com.product.filter;

import com.product.exception.CustomAuthenticationException;
import com.product.service.TokenBlacklistService;
import com.product.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Rama Gopal
 * Project Name - product-curd-backend-app
 */

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");

            // Validate header
            if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
                throw new CustomAuthenticationException("Authorization header missing or invalid.");
            }

            String jwt = authHeader.substring(7);
            if (ObjectUtils.isEmpty(jwt)) {
                throw new CustomAuthenticationException("JWT token is missing.");
            }

            String username;
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                throw new CustomAuthenticationException("JWT token expired.");
            } catch (MalformedJwtException | SignatureException | IllegalArgumentException e) {
                throw new CustomAuthenticationException("Invalid JWT token.");
            }

            if (StringUtils.isBlank(username) || tokenBlacklistService.isTokenBlacklisted(jwt)) {
                throw new CustomAuthenticationException("Invalid or blacklisted token.");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (!jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    throw new CustomAuthenticationException("Invalid JWT token.");
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (CustomAuthenticationException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false, \"message\":\"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false, \"message\":\"Internal server error\"}");
        }
    }
}