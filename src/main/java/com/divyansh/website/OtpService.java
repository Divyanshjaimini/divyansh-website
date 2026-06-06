package com.divyansh.website;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    // OTP store karne ke liye — email → {otp, timestamp}
    private final Map<String, long[]> otpStore = new HashMap<>();

    // OTP generate karo
    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        long expiry = System.currentTimeMillis() + (5 * 60 * 1000); // 5 minute
        otpStore.put(email, new long[]{Long.parseLong(otp), expiry});
        return otp;
    }

    // OTP verify karo
    public boolean verifyOtp(String email, String otp) {
        if (!otpStore.containsKey(email)) return false;
        long[] data = otpStore.get(email);
        long storedOtp = data[0];
        long expiry = data[1];
        if (System.currentTimeMillis() > expiry) {
            otpStore.remove(email);
            return false;
        }
        if (storedOtp == Long.parseLong(otp)) {
            otpStore.remove(email);
            return true;
        }
        return false;
    }
}
