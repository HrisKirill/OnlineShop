package com.khrystoforov.onlineshopapp.security;

import com.khrystoforov.onlineshopapp.entity.User;
import com.khrystoforov.onlineshopapp.service.CustomUserDetailsService;
import com.khrystoforov.onlineshopapp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.auth.message.AuthException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@AllArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) {
        try {
            String jwt = getJWTFromRequest(httpServletRequest);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                User userDetails = jwtTokenProvider.parseToken(jwt);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception ex) {
            log.error("Could not set user authentication");
            throw new RuntimeException("Could not set user authentication");
        }
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearToken = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return bearToken.split(" ")[1];
        }
        return null;
    }


}