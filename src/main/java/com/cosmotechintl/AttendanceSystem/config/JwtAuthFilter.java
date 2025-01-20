package com.cosmotechintl.AttendanceSystem.config;


import com.cosmotechintl.AttendanceSystem.service.JwtService;
import com.cosmotechintl.AttendanceSystem.utility.ResponseUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        System.out.println("Request URI: " + requestUri);  // Log the request URI

        if ("/auth/refreshToken".equals(requestUri) || ("/auth/logout".equals(requestUri))){
            filterChain.doFilter(request, response);  // Skip the JWT filter logic and continue to the controller
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            try {
                username = jwtService.extractUsername(token);
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is expired.");
                return;
            }
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    // Token is invalid, send custom error
//                    sendAuthenticationError(response, "The username or password is incorrect. Please try again.");
                    ResponseUtil.getValidationErrorResponse("Access Denied");//authentication error is handled by authentrypoint.
                    return;
                }
            }catch (UsernameNotFoundException ex) {
                // Handle case where the user is not found in the database
//                sendAuthenticationError(response, "The username or password is incorrect. Please try again.");
                ResponseUtil.getResourceNotFoundResponse("Not a Valid Username.");//this is handled by AuthException handled in the AuthService.
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
//    private void sendAuthenticationError(HttpServletResponse response, String message) throws IOException {
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        Map<String, Object> data = new HashMap<>();
//        data.put("timestamp", Calendar.getInstance().getTime());
//        data.put("exception", message);
//        response.getWriter().write(new ObjectMapper().writeValueAsString(data)); This private method has not been in used.
//    }
}
