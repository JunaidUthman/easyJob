package org.example.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    // before understanding what this class does, we need to understand what is the SecurityContextHolder

    //The SecurityContextHolder acts as a storage mechanism to hold the details of the currently authenticated user (the Authentication object) for the duration of a single request or session.
    //its is a static class that provides access to the SecurityContext,The SecurityContext is the lightweight object that actually holds the Authentication object.

    // so whats the JwtAuthFilter class does is : 
        //check if the request contain a valid JWT token(for every single request)
        //if yes , 
            // it extracts the username from the token
            //and store the user into the SecurityContextHolder (its stored temporarily for the duration of the request)

        //if not , it just passes the request to the next filter in the chain without setting any authentication details in the SecurityContextHolder

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        //extract the token from the "Authorization" header
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        // cheks if the user is already authenticated (to avoid re-authentication)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(jwt)) {

                //create a token object using the user details, this object will be stored in the SecurityContextHolder
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());//userdetails, password(null), and authorities(roles/permissions)

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));//set additional details about the authentication request (like IP address, session ID, etc.)
                SecurityContextHolder.getContext().setAuthentication(authToken);//aadd the authentication token to the SecurityContextHolder
            }
        }

        chain.doFilter(request, response);
    }
}
