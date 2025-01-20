package com.example.mimir.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "user")
@SecondaryTable(
        name = "user_history",
        pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id")
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, name = "google_auth_key")
    private String googleAuthKey;

    @Column(nullable = false, name = "created_at")
    private Date createdAt;

    @Column(nullable = false, name = "updated_at")
    private Date updatedAt;

    public String getUsername () {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getGoogleAuthKey () {
        return googleAuthKey;
    }

    public void setGoogleAuthKey (String googleAuthKey) {
        this.googleAuthKey = googleAuthKey;
    }

    public Date getCreatedAt () {
        return createdAt;
    }

    public void setCreatedAt (Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt () {
        return updatedAt;
    }

    public void setUpdatedAt (Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getId () {
        return id;
    }
}
