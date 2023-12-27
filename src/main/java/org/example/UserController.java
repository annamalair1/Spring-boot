package org.example;

import org.example.utilities.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
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
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (userDetails != null && password != null && !password.isEmpty()) {
            String encodedPassword = userDetails.getPassword();
            logger.info("Retrieved Encoded Password: {}", encodedPassword);
            if (passwordEncoder.matches(password, encodedPassword)) {
                String token = jwtUtil.generateToken(username); // Implement token generation
                return ResponseEntity.ok(token);

            } else {
                // If authentication successful, generate JWT token and return it
                return ResponseEntity.badRequest().body("Invalid username or password");
            }
        }
        else
        {
                // Handle missing user details or empty password
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
            }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Check if the username already exists
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        String encodedPassword = passwordEncoder.encode(password);
        // Create a new user and save it to the database
        User newUser = new User(username, encodedPassword); // Implement User entity
        userRepository.save(newUser);

        return ResponseEntity.ok("Signup successful");
    }
}
