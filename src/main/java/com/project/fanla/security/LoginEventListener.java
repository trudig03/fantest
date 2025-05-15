package com.project.fanla.security;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LoginEventListener {

    private static final Logger logger = LoggerFactory.getLogger(LoginEventListener.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        String username = auth.getName();
        String time = LocalDateTime.now().format(formatter);
        String authorities = auth.getAuthorities().toString();
        String remoteAddress = getRemoteAddress(auth);
        
        logger.info("LOGIN SUCCESS - Time: {}, User: {}, Authorities: {}, IP: {}", 
                time, username, authorities, remoteAddress);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        Authentication auth = event.getAuthentication();
        String username = auth.getName();
        String time = LocalDateTime.now().format(formatter);
        String exceptionMessage = event.getException().getMessage();
        String remoteAddress = getRemoteAddress(auth);
        
        logger.warn("LOGIN FAILED - Time: {}, User: {}, Reason: {}, IP: {}", 
                time, username, exceptionMessage, remoteAddress);
    }
    
    private String getRemoteAddress(Authentication authentication) {
        try {
            if (authentication.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
                return details.getRemoteAddress();
            }
            return "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
