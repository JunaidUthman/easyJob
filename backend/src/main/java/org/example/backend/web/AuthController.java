package org.example.backend.web;

import org.example.backend.entities.Role;
import org.example.backend.entities.User;
import org.example.backend.repositories.UserRepo;
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
    private final UserRepo userRepo;

    public AuthController(UserRepo userRepo , JwtUtil jwtUtil , UserService userService , AuthenticationManager  authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        User user = userService.registerUser(
                body.get("username"),
                body.get("email"),
                body.get("password"),
                body.get("userType")
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
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()
                    )
            );

            // Generate JWT
            String token = jwtUtil.generateToken(request.getEmail());

            // we do this only for extracting the roles from the user , we can do this to not repeat the fetching :  UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepo.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Return token + role(s)
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "roles", user.getRoles().stream()
                            .map(Role::getName)
                            .toList(),
                    "username", user.getUsername()
            ));

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "User not found"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("msg", "Invalid password"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("msg", "An error occurred during login"));
        }
    }


}
