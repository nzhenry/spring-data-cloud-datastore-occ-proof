package com.myapplication;

import com.google.gson.Gson;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;

public class WebTest {

    private static final Gson GSON = new Gson();

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    protected static <T> T getResponse(MvcResult result, Class<T> clazz) {
        try {
            String str = result.getResponse().getContentAsString();
            return GSON.fromJson(str, clazz);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
