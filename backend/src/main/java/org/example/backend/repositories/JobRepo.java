package org.example.backend.repositories;

import org.example.backend.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepo extends JpaRepository<Job, Long> {
    List<Job> findByCreatorId(Long creatorId);
}
