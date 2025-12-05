package com.luv2code.spring_boot_library.service;

import com.luv2code.spring_boot_library.dto.LoginRequest;
import com.luv2code.spring_boot_library.dto.RegisterRequest;
import com.luv2code.spring_boot_library.model.User;
import com.luv2code.spring_boot_library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(RegisterRequest request) {
        // Check if username already exists
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Create new user (in a real app, password should be hashed)
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // In production, use BCrypt or similar
        
        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isEmpty() || !user.get().getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        return user.get();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}





