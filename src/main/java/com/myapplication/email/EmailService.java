package com.myapplication.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    public void sendWelcomeEmail(String to) {
        log.info("Sending welcome email to {}", to);
    }
}
