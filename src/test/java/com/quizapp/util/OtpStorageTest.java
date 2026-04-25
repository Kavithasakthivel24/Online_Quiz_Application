package com.quizapp.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OtpStorageTest {

    @Test
    void testSaveAndVerifyOtp() {

        String email = "test@gmail.com";
        String otp = "123456";

        OtpStorage.saveOtp(email, otp);

        assertTrue(OtpStorage.verifyOtp(email, otp));
    }

    @Test
    void testWrongOtp() {

        String email = "test@gmail.com";

        OtpStorage.saveOtp(email, "123456");

        assertFalse(OtpStorage.verifyOtp(email, "000000"));
    }
}