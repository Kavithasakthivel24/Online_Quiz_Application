package com.quizapp.service;

import com.quizapp.entity.User;
import com.quizapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByUsername(String username) {

        Optional<User> user = userRepository.findByUsername(username);

        return user.orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User registerUser(User user) {
        return userRepository.save(user);
    }
}