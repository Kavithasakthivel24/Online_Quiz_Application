package com.quizapp.controller;

import com.quizapp.config.JwtFilter;
import com.quizapp.config.JwtUtil;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizController.class)
@AutoConfigureMockMvc(addFilters = false)
public class QuizControllerTest {

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizRepository quizRepository;

    @MockBean
    private QuestionRepository questionRepository;

    // ✅ TEST GET ALL QUIZZES
    @Test
    void testGetAllQuizzes() throws Exception {

        Quiz quiz1 = new Quiz();
        quiz1.setId(1L);
        quiz1.setTitle("Java Quiz");
        quiz1.setDescription("Basics");
        quiz1.setDuration(30);

        Quiz quiz2 = new Quiz();
        quiz2.setId(2L);
        quiz2.setTitle("Spring Quiz");
        quiz2.setDescription("Advanced");
        quiz2.setDuration(45);

        when(quizRepository.findAll()).thenReturn(List.of(quiz1, quiz2));

        when(questionRepository.countByQuizId(1L)).thenReturn(5L);
        when(questionRepository.countByQuizId(2L)).thenReturn(10L);

        mockMvc.perform(get("/api/quiz/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Java Quiz"))
                .andExpect(jsonPath("$[0].questionCount").value(5))
                .andExpect(jsonPath("$[1].title").value("Spring Quiz"))
                .andExpect(jsonPath("$[1].questionCount").value(10));
    }

    // ✅ TEST GET QUIZ BY ID - SUCCESS
    @Test
    void testGetQuizByIdSuccess() throws Exception {

        Quiz quiz = new Quiz();
        quiz.setId(1L);
        quiz.setTitle("Java Quiz");

        when(quizRepository.findById(1L)).thenReturn(Optional.of(quiz));

        mockMvc.perform(get("/api/quiz/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java Quiz"));
    }

    // ✅ TEST GET QUIZ BY ID - NOT FOUND
    @Test
    void testGetQuizByIdFail() throws Exception {

        when(quizRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/quiz/99"))
                .andExpect(status().isNotFound());
    }
}