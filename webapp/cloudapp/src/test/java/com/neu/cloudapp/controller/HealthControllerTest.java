package com.neu.cloudapp.controller;

import com.timgroup.statsd.StatsDClient;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(value = HealthController.class)
public class HealthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    StatsDClient statsDClient;
    @Test
    public void getStatusOk() throws Exception{

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                        .get("/healthz")
                        .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int status = result.getResponse().getStatus();
        Assert.assertEquals(200, status );
    }
}
