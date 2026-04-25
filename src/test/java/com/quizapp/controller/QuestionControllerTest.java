package com.quizapp.controller;

import com.quizapp.config.JwtFilter;
import com.quizapp.config.JwtUtil;
import com.quizapp.entity.Question;
import com.quizapp.entity.Quiz;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.QuizRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    private QuizRepository quizRepository;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private JwtUtil jwtUtil;

    // ✅ TEST GET QUESTIONS BY QUIZ
    @Test
    void testGetQuestionsByQuiz() throws Exception {

        Question q = new Question();
        q.setId(1L);
        q.setQuestion("What is Java?");

        when(questionRepository.findByQuizId(1L))
                .thenReturn(List.of(q));

        mockMvc.perform(get("/api/question/byQuiz/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question")
                        .value("What is Java?"));
    }

    // ✅ TEST SAVE QUESTION SUCCESS
    @Test
    void testSaveQuestionSuccess() throws Exception {

        Quiz quiz = new Quiz();
        quiz.setId(1L);

        when(quizRepository.findById(1L))
                .thenReturn(Optional.of(quiz));

        mockMvc.perform(post("/api/question/saveQuestion")
                .param("quizId", "1")
                .param("question", "What is Spring?")
                .param("optionA", "Framework")
                .param("optionB", "Language")
                .param("optionC", "DB")
                .param("optionD", "OS")
                .param("correctAnswer", "Framework"))
                .andExpect(status().isOk())
                .andExpect(content().string("Question saved successfully"));
    }

    // ❌ TEST SAVE QUESTION FAIL (QUIZ NOT FOUND)
    @Test
    void testSaveQuestionFail() throws Exception {

        when(quizRepository.findById(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/question/saveQuestion")
                .param("quizId", "1"))
                .andExpect(status().isNotFound());
    }
}