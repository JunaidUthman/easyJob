package org.example.backend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.enums.EducationLevel;

@Data
@NoArgsConstructor
public class ApplicationRequestDTO {
    // Job Info
    private Long jobId;
    private String jobTitle;
    private String jobDescription;

    // Applicant Info
    private Long applicantId;
    private String applicantName;
    private String applicantEmail;
    private String applicantImage; // Profile picture URL

    // Additional Applicant Details
    private EducationLevel educationLevel;
}
