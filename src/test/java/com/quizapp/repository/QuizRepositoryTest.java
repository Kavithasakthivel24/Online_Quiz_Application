package com.quizapp.repository;

import com.quizapp.entity.Quiz;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class QuizRepositoryTest {

    @Autowired
    private QuizRepository quizRepository;

    @Test
    void testSaveQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle("Java Test");

        Quiz saved = quizRepository.save(quiz);

        assertNotNull(saved.getId());
        assertEquals("Java Test", saved.getTitle());
    }
}