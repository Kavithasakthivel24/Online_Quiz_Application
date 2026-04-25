package com.quizapp.service;

import com.quizapp.entity.Question;
import com.quizapp.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    // ✅ Create Question
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    // ✅ Get questions by quizId
    public List<Question> getQuestionsByQuizId(Long quizId) {
        return questionRepository.findByQuizId(quizId);
    }

    // ✅ Get question by id
    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    // 🗑 Delete question
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}