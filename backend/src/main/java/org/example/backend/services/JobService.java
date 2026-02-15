package org.example.backend.services;

import lombok.AllArgsConstructor;
import org.example.backend.DTO.JobDTO;
import org.example.backend.entities.Job;
import org.example.backend.entities.User;
import org.example.backend.enums.ContractType;
import org.example.backend.enums.EducationLevel;
import org.example.backend.enums.JobType;
import org.example.backend.repositories.JobRepo;
import org.example.backend.repositories.UserRepo;
import org.springframework.stereotype.Service;
import org.example.backend.DTO.ApplicationRequestDTO;
import org.example.backend.entities.Candidate;
import org.example.backend.entities.Notification;
import org.example.backend.DTO.ApplicationRequestDTO;
import org.example.backend.entities.Candidate;
import org.example.backend.entities.Notification;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class JobService {

        private final JobRepo jobRepository;
        private final UserRepo userRepository;

        public List<JobDTO> getAllJobs() {
                List<JobDTO> jobs = jobRepository.findAll()
                                .stream()
                                .map(JobDTO::new)
                                .toList();

                return jobs;
        }

        public List<JobDTO> getJobsByUserEmail(String email) {
                User creator = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                return creator.getCreatedJobs()
                                .stream()
                                .map(JobDTO::new)
                                .toList();
        }

        public Map<String, String> applyJob(String email, Long jobId) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Job job = jobRepository.findById(jobId)
                                .orElseThrow(() -> new RuntimeException("Job not found"));

                // Check if user has already applied
                if (userRepository.hasAppliedToJob(user.getId(), jobId)) {
                        return Map.of("message", "You have already applied to this job", "alreadyApplied", "true");
                }

                user.getJobs().add(job);
                userRepository.save(user);

                return Map.of("message", "Application submitted successfully", "success", "true");
        }

        public List<JobDTO> getApplications(String email) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                return user.getJobs()
                                .stream()
                                .map(JobDTO::new)
                                .toList();
        }

        public Map<String, String> cancelApplication(String email, Long jobId) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Job job = jobRepository.findById(jobId)
                                .orElseThrow(() -> new RuntimeException("Job not found"));

                user.getJobs().remove(job);
                userRepository.save(user);

                return Map.of("message", "Application cancelled successfully");
        }

        public JobDTO createJob(String title, String description, String location, JobType type,
                        MultipartFile imageLogo, String creatorEmail, String company, String field, String function,
                        String contract_type, String experienceMin, String experienceMax, String educationLevel)
                        throws Exception {
                // Find the creator user
                User creator = userRepository.findByEmail(creatorEmail)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                String logoName = null;
                if (imageLogo != null && !imageLogo.isEmpty()) {
                        logoName = saveFile(imageLogo);
                }

                // Create new job
                Job job = new Job();
                job.setTitle(title);
                job.setDescription(description);
                job.setLocation(location);
                job.setImage(null); // 🔹 Background image removed
                job.setCompanyLogo(logoName);
                job.setType(type);
                job.setCreator(creator);
                job.setCompany(company);
                job.setField(field);
                job.setJobFunction(function);

                if (contract_type != null && !contract_type.isEmpty()) {
                        job.setContract_type(ContractType.valueOf(contract_type));
                }

                job.setExperienceMin(experienceMin);
                job.setExperienceMax(experienceMax);

                if (educationLevel != null && !educationLevel.isEmpty()) {
                        job.setEducationLevel(EducationLevel.valueOf(educationLevel));
                }

                // Save to database
                Job savedJob = jobRepository.save(job);

                return new JobDTO(savedJob);
        }

        public Map<String, String> deleteJob(Long jobId) {
                Job job = jobRepository.findById(jobId)
                                .orElseThrow(() -> new RuntimeException("Job not found"));

                jobRepository.delete(job);
                return Map.of("message", "Job deleted successfully");
        }

        public JobDTO updateJob(Long jobId, String title, String description, String location, JobType type,
                        MultipartFile imageLogo, String company, String field, String function,
                        String contract_type, String experienceMin, String experienceMax, String educationLevel)
                        throws Exception {

                Job job = jobRepository.findById(jobId)
                                .orElseThrow(() -> new RuntimeException("Job not found"));

                job.setTitle(title);
                job.setDescription(description);
                job.setLocation(location);
                job.setType(type);
                job.setCompany(company);
                job.setField(field);
                job.setJobFunction(function);

                if (imageLogo != null && !imageLogo.isEmpty()) {
                        String logoName = saveFile(imageLogo);
                        job.setCompanyLogo(logoName);
                }

                if (contract_type != null && !contract_type.isEmpty()) {
                        job.setContract_type(ContractType.valueOf(contract_type));
                }

                job.setExperienceMin(experienceMin);
                job.setExperienceMax(experienceMax);

                if (educationLevel != null && !educationLevel.isEmpty()) {
                        job.setEducationLevel(EducationLevel.valueOf(educationLevel));
                }

                Job updatedJob = jobRepository.save(job);
                return new JobDTO(updatedJob);
        }

        private String saveFile(MultipartFile file) throws Exception {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(System.getProperty("user.dir")).resolve("static/uploads/images");
                if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());
                return fileName;
        }

        public List<ApplicationRequestDTO> getRecruiterRequests(String email) {
                User recruiter = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                return recruiter.getCreatedJobs().stream()
                                .flatMap(job -> job.getUsers().stream()
                                                .map(applicant -> {
                                                        ApplicationRequestDTO dto = new ApplicationRequestDTO();
                                                        dto.setJobId(job.getId());
                                                        dto.setJobTitle(job.getTitle());
                                                        dto.setJobDescription(job.getDescription());
                                                        dto.setApplicantId(applicant.getId());
                                                        dto.setApplicantName(applicant.getUsername());
                                                        dto.setApplicantEmail(applicant.getEmail());

                                                        // Handle Candidate specific fields safely
                                                        if (applicant instanceof Candidate) {
                                                                Candidate candidate = (Candidate) applicant;
                                                                dto.setApplicantImage(candidate.getProfilePicture());
                                                                dto.setEducationLevel(candidate.getEducationLevel());
                                                        } else {
                                                                dto.setApplicantImage(null);
                                                                dto.setEducationLevel(null);
                                                        }

                                                        return dto;
                                                }))
                                .toList();
        }

        public Map<String, String> acceptApplication(Long jobId, Long applicantId) {
                User applicant = userRepository.findById(applicantId)
                                .orElseThrow(() -> new RuntimeException("Applicant not found"));

                Job job = jobRepository.findById(jobId)
                                .orElseThrow(() -> new RuntimeException("Job not found"));

                Notification notification = new Notification();
                notification.setMessage("Congratulations! Your application for the job '" + job.getTitle()
                                + "' has been accepted by " + job.getCompany() + ".");
                notification.setReadStatus(false);
                notification.setUser(applicant);

                if (applicant.getNotifications() == null) {
                        applicant.setNotifications(new java.util.ArrayList<>());
                }
                applicant.getNotifications().add(notification);

                userRepository.save(applicant);

                return Map.of("message", "Application accepted and notification sent.");
        }
}
