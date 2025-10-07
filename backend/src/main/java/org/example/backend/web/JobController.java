package org.example.backend.web;

import org.example.backend.entities.Job;

import org.example.backend.repositories.JobRepo;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobRepo jobRepository;

    public JobController(JobRepo jobRepository) {
        this.jobRepository = jobRepository;
    }

    // GET all jobs
    @GetMapping("/test")
    @CrossOrigin(origins = "http://localhost:4200")
    public Map<String, String> test() {
        Map<String, String> response = new HashMap<>();
        response.put("msg", "hello");
        return response;
    }

//    @GetMapping
//    public List<Job> getAllJobs() {
//        return jobRepository.findAll();
//    }

    // POST new job
    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobRepository.save(job);
    }
}
