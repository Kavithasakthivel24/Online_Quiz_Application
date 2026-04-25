package com.quizapp.service;

import com.quizapp.entity.Question;
import com.quizapp.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    // ✅ 1. CREATE QUESTION
    @Test
    void testCreateQuestion() {

        Question question = new Question();
        question.setQuestion("What is Java?");
        question.setOptionA("Language");
        question.setOptionB("OS");
        question.setOptionC("Database");
        question.setOptionD("Compiler");
        question.setCorrectAnswer("Language");

        when(questionRepository.save(question)).thenReturn(question);

        Question result = questionService.createQuestion(question);

        assertNotNull(result);
        assertEquals("What is Java?", result.getQuestion());

        verify(questionRepository, times(1)).save(question);
    }

    // ✅ 2. GET QUESTIONS BY QUIZ ID
    @Test
    void testGetQuestionsByQuizId() {

        Question q1 = new Question();
        q1.setQuestion("Q1");

        Question q2 = new Question();
        q2.setQuestion("Q2");

        when(questionRepository.findByQuizId(1L))
                .thenReturn(Arrays.asList(q1, q2));

        List<Question> result = questionService.getQuestionsByQuizId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Q1", result.get(0).getQuestion());

        verify(questionRepository, times(1)).findByQuizId(1L);
    }

    // ✅ 3. GET QUESTION BY ID
    @Test
    void testGetQuestionById() {

        Question question = new Question();
        question.setId(1L);
        question.setQuestion("Java Question");

        when(questionRepository.findById(1L))
                .thenReturn(Optional.of(question));

        Question result = questionService.getQuestionById(1L);

        assertNotNull(result);
        assertEquals("Java Question", result.getQuestion());

        verify(questionRepository, times(1)).findById(1L);
    }

    // ✅ 4. DELETE QUESTION
    @Test
    void testDeleteQuestion() {

        doNothing().when(questionRepository).deleteById(1L);

        questionService.deleteQuestion(1L);

        verify(questionRepository, times(1)).deleteById(1L);
    }
}