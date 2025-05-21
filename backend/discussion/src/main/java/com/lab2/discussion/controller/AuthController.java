package com.lab2.discussion.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lab2.discussion.model.User;
import com.lab2.discussion.repository.UserRepository;
import com.lab2.discussion.security.JwtUtil;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private BCryptPasswordEncoder encoder;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");
        userRepo.save(user);
        return "User registered";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User loginRequest) {
        User user = userRepo.findByUsername(loginRequest.getUsername()).orElseThrow();
        if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
            return Map.of("token", token, "username", user.getUsername(), "role", user.getRole());
        }
        throw new RuntimeException("Invalid login");
    }
}
