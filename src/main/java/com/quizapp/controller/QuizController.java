package com.quizapp.controller;

import com.quizapp.entity.Quiz;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.QuizRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*")
public class QuizController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // ✅ GET ALL QUIZZES
    @GetMapping("/all")
    public List<Map<String, Object>> getAllQuizzes() {

        List<Quiz> quizzes = quizRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Quiz quiz : quizzes) {

            long questionCount = questionRepository.countByQuizId(quiz.getId());

            Map<String, Object> map = new HashMap<>();
            map.put("id", quiz.getId());
            map.put("title", quiz.getTitle());
            map.put("description", quiz.getDescription());
            map.put("duration", quiz.getDuration());
            map.put("questionCount", questionCount);

            result.add(map);
        }

        return result;
    }

    // ✅ GET QUIZ BY ID (FINAL FIX)
    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {

        return quizRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}