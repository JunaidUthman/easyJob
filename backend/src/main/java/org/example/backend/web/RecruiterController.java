package org.example.backend.web;

import lombok.AllArgsConstructor;
import org.example.backend.DTO.ApplicationRequestDTO;
import org.example.backend.services.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/recruiter")
public class RecruiterController {

    private final JobService jobService;

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        return ResponseEntity.ok(Map.of("message", "RecruiterController is reachable", "status", "OK"));
    }

    @GetMapping("/requests")
    public List<ApplicationRequestDTO> getRecruiterRequests(Authentication authentication) {
        String email = authentication.getName();
        System.out.println("Processing getRecruiterRequests for email: " + email);
        List<ApplicationRequestDTO> requests = jobService.getRecruiterRequests(email);
        System.out.println("Found " + requests.size() + " requests");
        return requests;
    }

    @PostMapping("/requests/{jobId}/{applicantId}/accept")
    public ResponseEntity<Map<String, String>> acceptApplication(@PathVariable Long jobId,
            @PathVariable Long applicantId) {
        return ResponseEntity.ok(jobService.acceptApplication(jobId, applicantId));
    }
}
