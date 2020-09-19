package com.myapplication.user;

import com.myapplication.WebTest;
import com.myapplication.email.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public class UserControllerTest extends WebTest {

    @MockBean
    private EmailService emailService;

    @Test
    public void testWithoutTransaction() {

        String id = UUID.randomUUID().toString();
        String url = "/user/" + id;

        getUserNTimesAsync(url, 10);

        verify(emailService, atLeast(2))
                .sendWelcomeEmail(eq(id));
    }

    @Test
    public void testWithTransaction() {

        String id = UUID.randomUUID().toString();
        String url = "/user/" + id + "?transactional=true";

        getUserNTimesAsync(url, 10);

        verify(emailService, times(1))
                .sendWelcomeEmail(eq(id));
    }

    private List<User> getUserNTimesAsync(String path, int count) {
        List<CompletableFuture<User>> results = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            results.add(CompletableFuture.supplyAsync(() -> getUser(path)));
        }
        return results.stream()
                .map(this::getResult)
                .collect(Collectors.toList());
    }

    private <T> T getResult(Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User getUser(String path) {
        try {
            MvcResult mvcResult = mockMvc.perform(get(path))
                    .andExpect(status().isOk())
                    .andReturn();
            return getResponse(mvcResult, User.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
