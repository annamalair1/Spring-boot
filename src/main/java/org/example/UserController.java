package org.example;

import org.example.utilities.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/signup")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "OPTIONS")
                .allowedHeaders("*");
    }
    @Autowired
    private JWTUtil jwtUtil;
    @GetMapping("/users")
    public List<String> getUsers(){
        return Arrays.asList("Anna","User1","User2");
    }
    @Autowired
    private UserRepository userRepository; // Assuming you have a UserRepository

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Validate username and password (you may use Spring Security for this)
        // Perform authentication logic

        // If authentication successful, generate JWT token and return it
        String token = jwtUtil.generateToken(username); // Implement token generation

        return ResponseEntity.ok(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Check if the username already exists
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Create a new user and save it to the database
        User newUser = new User(username, password); // Implement User entity
        userRepository.save(newUser);

        return ResponseEntity.ok("Signup successful");
    }
}
