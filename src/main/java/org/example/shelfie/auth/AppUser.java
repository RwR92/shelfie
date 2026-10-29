package org.example.shelfie.auth;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;

    protected AppUser() {}

    public AppUser(String username) {
        this.username = username;
    }

    public Long getId(){
        return id;
    }
    public String getUsername() {
        return username;
    }
}
