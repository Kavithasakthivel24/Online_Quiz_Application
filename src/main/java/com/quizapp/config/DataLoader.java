package com.quizapp.config;

import com.quizapp.entity.User;

import com.quizapp.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(UserRepository repo, PasswordEncoder encoder) {
        return args -> {

            if (repo.findByUsername("admin").isEmpty()) {

                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("123"));
                admin.setRole("ADMIN");

                repo.save(admin);

                System.out.println("✅ Admin Created: admin / 123");
            }
        };
    }
}