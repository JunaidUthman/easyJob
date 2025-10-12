package org.example.backend.services;

import org.example.backend.entities.Role;
import org.example.backend.entities.User;
import org.example.backend.repositories.RoleRepository;
import org.example.backend.repositories.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserService{
    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    private final RoleRepository roleRepository;

    public UserService(UserRepo userRepository, PasswordEncoder passwordEncoder ,  RoleRepository roleRepository , RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }



    public User registerUser(String username, String email, String rawPassword, String roleName) {

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return null;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));

        // Save first to have an ID (needed for @ManyToMany join table)
        user = userRepository.save(user);

        // Assign role using your RoleService
        roleService.assignRoleToUser(user, roleName);

        return user;
    }

}
