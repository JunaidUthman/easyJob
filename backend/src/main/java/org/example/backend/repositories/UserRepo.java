package org.example.backend.repositories;

import org.example.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // we use optional to return a user , but if its null , its ok
}
