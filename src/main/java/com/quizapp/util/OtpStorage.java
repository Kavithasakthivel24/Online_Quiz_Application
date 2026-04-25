package com.quizapp.util;

import java.util.HashMap;
import java.util.Map;

public class OtpStorage {

    private static Map<String, String> otpMap = new HashMap<>();
    private static Map<String, Long> otpTimeMap = new HashMap<>();

    private static final long EXPIRY_TIME = 5 * 60 * 1000; // ✅ 5 minutes

    public static void saveOtp(String email, String otp) {
        otpMap.put(email, otp);
        otpTimeMap.put(email, System.currentTimeMillis());
    }

    public static boolean verifyOtp(String email, String otp) {

        if (!otpMap.containsKey(email)) return false;

        long storedTime = otpTimeMap.get(email);
        long currentTime = System.currentTimeMillis();

        // ✅ FIXED LOGIC
        if ((currentTime - storedTime) > EXPIRY_TIME) {
            otpMap.remove(email);
            otpTimeMap.remove(email);
            return false;
        }

        return otpMap.get(email).equals(otp);
    }

    public static void clearOtp(String email) {
        otpMap.remove(email);
        otpTimeMap.remove(email);
    }
}