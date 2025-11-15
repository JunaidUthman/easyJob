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

            //UsernamePasswordAuthenticationToken is an object that Spring Security uses to encapsulate the username and password and authenticated boolean(false by default)
            //once the function authenticate() secceeds it returns an Authentication object containing : A fully loaded UserDetails object, Authenticated = true,Authorities

            //if the function do not secceed it throws an exception(see the exceptions below)

            //now lets understand how authenticationManager.authenticate() works :
            // this function exists in AuthenticationManager interface. its implemented by ProviderManager class.
            //this class holds a list of different AuthenticationProvider instances. each one is responsible for checking if a user is authenticated in a specific way. exemples : DaoAuthenticationProvider(the default one) , JwtAuthenticationProvider , LdapAuthenticationProvider
            //in this case , the DaoAuthenticationProvider is used by default.it calls CostumeUserService.loadUserByUsername(email) to load user details(this part u can code it urself). and then compares the provided password with the stored password(using PasswordEncoder) (this part is handled internally by Spring security, u can ovverride it but mablanch).
            // once everything is ok , it returns a fully populated Authentication object.



            // Generate JWT
            String token = jwtUtil.generateToken(request.getEmail());

            // we do this only for extracting the roles from the user , we can do this to not repeat the fetching :  UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userRepo.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Return token + role(s)
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "ExpirationTime", jwtUtil.getExpirationTime(),
                    "roles", user.getRoles().stream()
                            .map(Role::getName)
                            .toList(),
                    "username", user.getUsername()
            ));

        } catch (UsernameNotFoundException e) {//cannot find a user with the given principal (email/username).
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "User not found"));
        } catch (BadCredentialsException e) {//. Thrown if the username/email is correct but the password does not match
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("msg", "Invalid password"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("msg", "An error occurred during login"));
        }
        //there is mor exceptions like :
        //DisabledException : Thrown if an authentication request is rejected because the account is disabled.
        //LockedException : Thrown if the user account is locked (e.g., due to too many failed login attempts).
        //AccountExpiredException : Thrown if the user account has expired.
    }


}
