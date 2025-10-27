package org.example.backend.DTO;

import org.example.backend.entities.Job;

public class JobDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String image;
    private String type;

    public JobDTO(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.location = job.getLocation();
//        if (job.getImage() != null && !job.getImage().isEmpty()) {
//            this.image = "http://localhost:8080/jobs/image/" + job.getImage();
//        } else {
//            this.image = null; // or some placeholder URL
//        }
        this.type = job.getType().toString();
        this.image= job.getImage();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
