package com.quizapp.service;

import com.quizapp.entity.Quiz;
import com.quizapp.repository.QuizRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    // ✅ CREATE QUIZ
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }
    
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    // ✅ GET QUIZ BY ID
    public Quiz getQuizById(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    // ✅ DELETE QUIZ
    public void deleteQuiz(Long id) {
        quizRepository.deleteById(id);
    }
}