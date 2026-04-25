package com.quizapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quizapp.entity.Question;
import com.quizapp.entity.Quiz;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.QuizRepository;

@RestController
@RequestMapping("/api/question")
@CrossOrigin(origins = "*")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    // ✅ GET QUESTIONS BY QUIZ
    @GetMapping("/byQuiz/{quizId}")
    public List<Question> getByQuiz(@PathVariable Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    // ✅ SAVE QUESTION (REST FIXED)
    @PostMapping("/saveQuestion")
    public ResponseEntity<?> saveQuestion(@ModelAttribute Question q,
                                          @RequestParam Long quizId) {

        Optional<Quiz> quiz = quizRepository.findById(quizId);

        if (quiz.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        q.setQuiz(quiz.get());
        questionRepository.save(q);

        return ResponseEntity.ok("Question saved successfully");
    }
}