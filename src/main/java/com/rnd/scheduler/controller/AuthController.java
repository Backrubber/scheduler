package com.rnd.scheduler.controller;

import com.rnd.scheduler.model.User;
import com.rnd.scheduler.repository.UserRepository;
import com.rnd.scheduler.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtil jwtUtils;
    @PostMapping("/signin")
    public String authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtUtils.generateToken(userDetails.getUsername());
    }
    @PostMapping("/signup")
    public String registerUser(@RequestBody User user) {
        //check for name availability
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Error: Username is already taken!";
        }
        //make new user and append it to the database
        User newUser = new User(
                null,
                user.getUsername(),
                encoder.encode(user.getPassword()),
                user.getFirstName(),
                user.getLastName(),
                null,
                null
        );
        userRepository.save(newUser);
        return "User registered successfully!";
    }
}
