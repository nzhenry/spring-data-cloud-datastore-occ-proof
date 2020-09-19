package com.myapplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public class MyControllerTest extends WebTest {

    @Test
    public void testWithoutTransaction() {

        String id = UUID.randomUUID().toString();
        String url = "/my-entity/" + id;

        int maxCount = putEntities(url, 10)
                .mapToInt(MyEntity::getSaveCount)
                .max()
                .getAsInt();

        assertThat(maxCount, lessThan(10));
    }

    @Test
    public void testWithTransaction() {

        String id = UUID.randomUUID().toString();
        String url = "/my-entity/" + id + "?transactional=true";

        int maxCount = putEntities(url, 10)
                .mapToInt(MyEntity::getSaveCount)
                .max()
                .getAsInt();

        assertEquals(10, maxCount);
    }

    private Stream<MyEntity> putEntities(String path, int count) {
        List<CompletableFuture<MyEntity>> results = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            results.add(CompletableFuture.supplyAsync(() -> putEntity(path)));
        }
        return results.stream().map(this::getResult);
    }

    private <T> T getResult(Future<T> future) {
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MyEntity putEntity(String path) {
        try {
            MvcResult mvcResult = mockMvc.perform(put(path))
                    .andExpect(status().isOk())
                    .andReturn();
            return getResponse(mvcResult, MyEntity.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
