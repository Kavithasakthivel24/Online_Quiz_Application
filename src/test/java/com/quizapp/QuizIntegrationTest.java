package com.quizapp;

import com.quizapp.entity.Quiz;
import com.quizapp.repository.QuizRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class QuizIntegrationTest {

    @Autowired
    private QuizRepository quizRepository;

    @Test
    void testSaveAndFetchQuiz() {

        Quiz quiz = new Quiz();
        quiz.setTitle("Spring Boot Integration Test");

        Quiz saved = quizRepository.save(quiz);

        Quiz fetched = quizRepository.findById(saved.getId()).orElse(null);

        assertNotNull(fetched);
        assertEquals("Spring Boot Integration Test", fetched.getTitle());
    }

    @Test
    void testDeleteQuiz() {

        Quiz quiz = new Quiz();
        quiz.setTitle("Delete Test");

        Quiz saved = quizRepository.save(quiz);

        quizRepository.deleteById(saved.getId());

        assertFalse(quizRepository.findById(saved.getId()).isPresent());
    }
}