package com.example.paymentprovider;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

//@SpringBootTest
class PaymentProviderApplicationTests {

    @Test
    void contextLoads() {
        String username = "mamadoo";
//        String password = "$2a$10$detHsGHj.9WTr.7AdUsyA.0H/2aAktXwBQS/RCJJD7jisYhY6XumC";
        String password = "123456789";
        String input = username + ":" + password;
//        System.out.println(new BCryptPasswordEncoder().encode(username + ":" + password));
//        System.out.println(Base64.getEncoder().encodeToString(input.getBytes()));




    }

}
