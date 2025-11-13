package org.example.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.example.backend.enums.JobType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "jobs")
@AllArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private String image;
    @Enumerated(EnumType.STRING)
    private JobType type;

    public Job(Long id,String title, String description, String location, String image,JobType type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.image = image;
        this.type = type;
    }


    // 🔹 Job created by a User (the recruiter)
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    // 🔹 Users who applied
    @ManyToMany(mappedBy = "jobs")
    private Set<User> users = new HashSet<>();

    public Job() {}


    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }

    public String getImage() {
        return image;
    }
    public void setImage(String image) { this.image = image; }

    public JobType getType() {
        return type;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                ", type=" + type +
                ", creator=" + creator +
                ", users=" + users +
                '}';
    }
}
