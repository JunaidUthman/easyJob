package org.example.backend.services;

import org.example.backend.entities.Role;
import org.example.backend.entities.User;
import org.example.backend.repositories.RoleRepository;
import org.example.backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepo userRepository;

    /**
     * Ensure a role exists in DB, and return it.
     */
    private Role getOrCreateRole(String roleName) {
        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isPresent()) {
            return roleOpt.get();
        }

        Role newRole = new Role(roleName);
        return roleRepository.save(newRole);
    }

    /**
     * Assign a role to a user and save them.
     */
    @Transactional
    public void assignRoleToUser(User user, String roleName) {
        Role role = getOrCreateRole(roleName);
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
