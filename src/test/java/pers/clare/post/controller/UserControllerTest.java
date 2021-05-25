package pers.clare.post.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import pers.clare.common.result.ResultRefresh;
import pers.clare.post.data.entity.User;
import pers.clare.post.util.JsonUtil;

import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.profiles.active=test"}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {


    private static final HttpHeaders headers = new HttpHeaders();

    static {
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    @Order(1)
    void insert() throws IOException {
        String name = String.valueOf(new Random().nextInt(100));
        String responseBody = restTemplate.postForObject("http://localhost:" + port + "/user"
                , new HttpEntity<>("name=" + name, headers)
                , String.class
        );

        ResultRefresh<User> response = JsonUtil.decode(responseBody, new TypeReference<ResultRefresh<User>>() {
        });

        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(name, response.getData().getName());
        assertTrue(response.getRefresh().getUsers().size() > 0);
    }
}
