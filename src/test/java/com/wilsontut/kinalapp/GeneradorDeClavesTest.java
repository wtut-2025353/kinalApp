package com.wilsontut.kinalapp;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneradorDeClavesTest {

    @Test
    public void generatePasswords() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("ADMIN_HASH: " + encoder.encode("admin123"));
    }
}
