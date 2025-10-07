package org.example.backend.repositories;

import org.example.backend.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepo extends JpaRepository<Job, Long> {
}
