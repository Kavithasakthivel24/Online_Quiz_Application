package com.quizapp.controller;


import com.quizapp.service.EmailService;
import com.quizapp.entity.Question;
import com.quizapp.entity.Quiz;
import com.quizapp.entity.Result;
import com.quizapp.entity.User;
import com.quizapp.repository.QuestionRepository;
import com.quizapp.repository.QuizRepository;
import com.quizapp.repository.ResultRepository;
import com.quizapp.repository.UserRepository;
import com.quizapp.util.OtpStorage;
import com.quizapp.util.OtpUtil;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";   // opens login.html
    }

    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @PostMapping("/signup")
    @ResponseBody
    public String signup(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        //System.out.println("USER SAVED: " + user.getUsername());
        //System.out.println("EMAIL: " + user.getEmail());

        // ✅ SEND EMAIL HERE
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            System.out.println("EMAIL SENDING STARTED...");
            emailService.sendRegistrationEmail(user.getEmail(), user.getUsername());
            System.out.println("EMAIL SENT SUCCESSFULLY...");
        }

        return "User Registered Successfully";
    }

    @PostMapping("/doLogin")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        System.out.println("LOGIN HIT");

        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            System.out.println("USER NOT FOUND");
            return "redirect:/login?error";
        }

        boolean match = passwordEncoder.matches(password, user.getPassword());

        System.out.println("MATCH RESULT: " + match);

        if (match) {
            session.setAttribute("user", user);

            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/dashboard";
            } else {
                return "redirect:/user_dashboard";
            }
        }

        return "redirect:/login?error";
    }
    
    @GetMapping("/user_dashboard")
    public String userDashboard() {
        return "user_dashboard";
    }

    @GetMapping("/users")
    @ResponseBody
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/create_test")
    public String createTestPage() {
        return "create_test"; // create_test.html
    }

    @GetMapping("/view_result")
    public String viewResultPage() {
        return "view_result"; // view_result.html
    }
    
    @GetMapping("/register")
    public String signup() {
        return "register"; 
    }
    
    
    @Autowired
    private QuizRepository quizRepository;

    @PostMapping("/saveQuiz")
    public String saveQuiz(@RequestParam String title,
                           @RequestParam int timePerQuestion,
                           @RequestParam String description) {
    	
    	System.out.println("SAVE QUIZ CALLED");

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setDescription(description);
        quiz.setDuration(timePerQuestion);

        quizRepository.save(quiz);

        return "redirect:/dashboard";
    }
    
//    @GetMapping("/add-question")
//    public String addQuestionPage() {
//        return "add_question";
//    }
    
    @GetMapping("/add-question")
    public String addQuestionPage(@RequestParam Long quizId) {
        return "add_question";
    }
    
    
    @GetMapping("/quiz")
    public String startQuiz(@RequestParam(required = false) Long quizId) {
        return "view_test";
    }
    
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @PostMapping("/view-Test")
    public String saveQuestion(@ModelAttribute Question question,
                               @RequestParam Long quizId) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        question.setQuiz(quiz);   // 🔥 IMPORTANT LINE

        questionRepository.save(question);

        return "redirect:/add-question?quizId=" + quizId;
    }

    @PostMapping("/saveQuestion")
    public String saveQuestion(@RequestParam Long quizId,
                               @RequestParam String question,
                               @RequestParam String optionA,
                               @RequestParam String optionB,
                               @RequestParam String optionC,
                               @RequestParam String optionD,
                               @RequestParam String correctAnswer) {

        Quiz quiz = quizRepository.findById(quizId).get();

        Question q = new Question();
        q.setQuestion(question);
        q.setOptionA(optionA);
        q.setOptionB(optionB);
        q.setOptionC(optionC);
        q.setOptionD(optionD);
        q.setCorrectAnswer(correctAnswer);
        q.setQuiz(quiz); // 🔥 VERY IMPORTANT

        questionRepository.save(q);

        return "redirect:/dashboard";
    }
    
    @GetMapping("/view-result")
    public String viewResult(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        List<Result> results = resultRepository.findByUser(user);

        model.addAttribute("results", results);

        return "view_result";
    }
    
    @GetMapping("/api/results")
    @ResponseBody
    public List<Result> getUserResults(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return resultRepository.findByUser(user);
    }
    
    @GetMapping("/api/results/all")
    @ResponseBody
    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }
    
    @GetMapping("/api/result")
    @ResponseBody
    public List<Result> getUserResult(HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return List.of();
        }

        return resultRepository.findByUser(user);
    }
    
    
    @GetMapping("/api/questions")
    @ResponseBody
    public Map<String, Object> getQuestions(@RequestParam Long quizId) {

        Quiz quiz = quizRepository.findById(quizId).get();
        List<Question> questions = questionRepository.findByQuizId(quizId);

        Map<String, Object> response = new HashMap<>();

        response.put("questions", questions);

        // 🔥 TOTAL TIME = time per question * number of questions
        response.put("duration", quiz.getDuration() * questions.size());

        return response;
    }
    
    @Autowired
    private ResultRepository resultRepository;
    
    @PostMapping("/submitQuiz")
    @ResponseBody
    public String submitQuiz(@RequestBody Map<String, Object> payload,
                             HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "User not logged in";
        }

        Long quizId = Long.valueOf(payload.get("quizId").toString());
        Map<String, String> answers = (Map<String, String>) payload.get("answers");

        Quiz quiz = quizRepository.findById(quizId).get();

        // ✅ FIX: Fetch questions properly
        List<Question> questions = questionRepository.findByQuizId(quizId);

        int score = 0;

        for (Question q : questions) {
            String ans = answers.get("q" + q.getId());

            if (ans != null && ans.equals(q.getCorrectAnswer())) {
                score++;
            }
        }

        // ✅ SAVE RESULT
        Result result = new Result();
        result.setUser(user);
        result.setQuiz(quiz);
        result.setScore(score);
        result.setTotal(questions.size());

        resultRepository.save(result);

        System.out.println("RESULT SAVED: " + score);

        return "Success";
    }
    
    
    @GetMapping("/start-test")
    public String startUserQuiz(@RequestParam Long quizId) {
        return "Test";  // ✅ user test page
    }
    
    @GetMapping("/user_viewresult")
    public String userViewResult(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        List<Result> results = resultRepository.findByUser(user);
        model.addAttribute("results", results);

        return "user_viewresult"; // 👉 create user_viewresult.html
    }
    
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot_password"; // create forgot_password.html
    }
    
    @PostMapping("/forgot-password_otp")
    @ResponseBody
    public String sendOtp(@RequestParam String email) {

        System.out.println("FORGOT PASSWORD HIT: " + email);

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return "Email not registered!";
        }

        // 🔐 Generate OTP
        String otp = OtpUtil.generateOtp();
        OtpStorage.saveOtp(email, otp);

        System.out.println("OTP GENERATED: " + otp);

        // 📧 Send OTP Email
        emailService.sendOtpEmail(email, otp);

        return "OTP sent successfully to your email";
    }
    
    @GetMapping("/reset-password-page")
    public String resetPasswordPage() {
        return "reset_password";
    }
    
    @PostMapping("/reset-password")
    @ResponseBody
    public String resetPassword(@RequestParam String email,
                               @RequestParam String otp,
                               @RequestParam String newPassword) {

        boolean isValid = OtpStorage.verifyOtp(email, otp);

        if (!isValid) {
            return "OTP expired or invalid!";
        }

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return "User not found!";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // clear OTP after success
        OtpStorage.clearOtp(email);

        return "Password reset successful!";
    }
    

    
}