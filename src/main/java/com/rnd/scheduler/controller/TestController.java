package com.rnd.scheduler.controller;

import com.rnd.scheduler.model.User;
import com.rnd.scheduler.security.JwtUtil;
import com.rnd.scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }
    @GetMapping("/user")
    public User userAccess(@RequestHeader("Authorization") String bearerToken) {
        bearerToken = bearerToken.replace("Bearer ", "");
        return userRepository.findByUsername(jwtUtil.getUsernameFromToken(bearerToken));
    }
}