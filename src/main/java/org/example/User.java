package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;
    // Default constructor
    public User() {
        // Default constructor
    }

    // Constructor with arguments
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Constructors, getters, setters
}
