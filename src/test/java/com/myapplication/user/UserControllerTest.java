package com.myapplication.user;

import com.myapplication.WebTest;
import com.myapplication.email.Email;
import com.myapplication.email.EmailRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public class UserControllerTest extends WebTest {

    @Autowired
    private EmailRepo emailRepo;

    @Test
    public void testWithoutTransaction() {

        String id = UUID.randomUUID().toString();
        String url = "/user/" + id;

        getUserNTimesAsync(url, 10);

        assertThat(getEmailCount(id), greaterThan(1));
    }

    @Test
    public void testWithTransaction() {

        String id = UUID.randomUUID().toString();
        String url = "/user/" + id + "?transactional=true";

        getUserNTimesAsync(url, 10);

        assertEquals(getEmailCount(id), 1);
    }

    private int getEmailCount(String id) {
        List<Email> emails = stream(emailRepo.findAll().spliterator(),false)
                .filter(email -> id.equals(email.getTo()))
                .collect(Collectors.toList());
        return emails.size();
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
