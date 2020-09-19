package com.myapplication.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final EmailRepo emailRepo;

    public EmailService(EmailRepo emailRepo) {
        this.emailRepo = emailRepo;
    }

    public Email sendWelcomeEmail(String to) {
        return emailRepo.save(new Email(to));
    }
}
