package org.example.backend.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private boolean readStatus = false;

    // Many-to-One with User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Notification() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isReadStatus() { return readStatus; }
    public void setReadStatus(boolean readStatus) { this.readStatus = readStatus; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
