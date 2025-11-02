package org.example.backend.web;

import org.example.backend.DTO.JobDTO;
import org.example.backend.entities.Job;

import org.example.backend.entities.User;
import org.example.backend.enums.JobType;
import org.example.backend.repositories.JobRepo;
import org.example.backend.repositories.UserRepo;
import org.example.backend.security.AuthRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobRepo jobRepository;
    private final UserRepo userRepository;

    public JobController(JobRepo jobRepository, UserRepo userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("getAllJobs")
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        List<JobDTO> jobs = jobRepository.findAll()
                .stream()
                .map(JobDTO::new)
                .toList();

        return ResponseEntity.ok(jobs);
    }

    // get jibs by the user authenticated
    @GetMapping("/getJobs")
    public ResponseEntity<List<JobDTO>> getJobsById() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User creator = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<JobDTO> jobDTOs = creator.getCreatedJobs()
                    .stream()
                    .map(JobDTO::new)
                    .toList();

            System.out.println("Returning jobs count: " + jobDTOs.size());
            return ResponseEntity.ok(jobDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws MalformedURLException {
        Path path = Paths.get("static/uploads/images").resolve(filename);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // or dynamically detect
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<Map<String, String>> applyJob(@PathVariable Long jobId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        creator.getJobs().add(job);
        userRepository.save(creator);

        return ResponseEntity.ok(Map.of("message", "Job applied"));
    }

    @GetMapping("/getApplications")
    public ResponseEntity<List<JobDTO>> getApplications() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User creator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        List<JobDTO> jobsApplyed = creator.getJobs()
                .stream()
                .map(JobDTO::new)
                .toList();

        return ResponseEntity.ok(jobsApplyed);
    }

    @DeleteMapping("/cancelApplication/{jobId}")
    public ResponseEntity<Map<String, String>> cancelApplication(@PathVariable Long jobId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));

        user.getJobs().remove(job);
        userRepository.save(user);


        return ResponseEntity.ok(Map.of("message", "Application cancelled successfully"));
    }




    @PostMapping("/createJob")
    public ResponseEntity<JobDTO> createJob(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String location,
            @RequestParam(required = true) JobType type,
            @RequestParam(required = false) MultipartFile image
    ) {
        try {
            // Find the creator user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();  // Email from token

            // Find user by email
            User creator = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String imageName = null;
            if (image != null && !image.isEmpty()) {
                // Generate unique filename
                imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                String uploadDir = "static/uploads/images/"; // choose your folder
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(imageName);
                Files.write(filePath, image.getBytes());
            }

            // Create new job
            Job job = new Job();
            job.setTitle(title);
            job.setDescription(description);
            job.setLocation(location);
            job.setImage(imageName);
            job.setType(type);
            job.setCreator(creator);

            // Save to database
            Job savedJob = jobRepository.save(job);

            JobDTO jobDTO = new JobDTO(savedJob);

            // Return saved job with CREATED status
            return ResponseEntity.status(HttpStatus.CREATED).body(jobDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
