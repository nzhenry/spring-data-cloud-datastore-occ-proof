package com.myapplication.user;

import com.myapplication.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/{id}")
    public User get(
            @PathVariable String id,
            @RequestParam(defaultValue = "false") boolean transactional) {
        User user = transactional
                ? userService.getUserWithTransaction(id)
                : userService.getUser(id);
        if(user.isNew()) {
            log.info("User created at {}", user.getCreatedAt().getTime());
            emailService.sendWelcomeEmail(user.getId());
        } else {
            log.info("User found");
        }
        return user;
    }
}
