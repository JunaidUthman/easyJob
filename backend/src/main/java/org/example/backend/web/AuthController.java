package org.example.backend.web;

import org.example.backend.entities.User;
import org.example.backend.security.AuthRequest;
import org.example.backend.security.JwtUtil;
import org.example.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController //this is used so java converts any Java object returned in the ResponseEntity body into JSON.
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil , UserService userService , AuthenticationManager  authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        User user = userService.registerUser(
                body.get("username"),
                body.get("email"),
                body.get("password")
        );
        if(user == null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("msg", "User already exists"));
        }
        return ResponseEntity.ok(Map.of("msg", "User registered!", "id", user.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    // this object cause a call to loadUserByUsername from UserDetailsService class to check the if the user exists and check his password (if not , an exception will be thrown)
                    // u can override this function by creating a class that extends from UserDetailsService
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())// here ur supposed to pass the username , bbut overrided loadUserByUsername so it uses email as a unique attribute
            );

            String token = jwtUtil.generateToken(request.getEmail());
            return ResponseEntity.ok(Map.of("token", token));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "User not found"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("msg", "Invalid password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("msg", "an error have accured in the backend"));
        }
    }

}
