package com.quizapp.service;

import com.quizapp.entity.Quiz;
import com.quizapp.repository.QuizRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuizService quizService;

    @Test
    void testGetAllQuizzes() {

        Quiz q1 = new Quiz();
        q1.setId(1L);
        q1.setTitle("Java");

        Quiz q2 = new Quiz();
        q2.setId(2L);
        q2.setTitle("Spring Boot");

        when(quizRepository.findAll()).thenReturn(Arrays.asList(q1, q2));

        List<Quiz> result = quizService.getAllQuizzes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getTitle());

        verify(quizRepository, times(1)).findAll();
    }
}