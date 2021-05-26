package pers.clare.post.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import pers.clare.common.result.ResultData;
import pers.clare.post.data.entity.Post;
import pers.clare.post.data.entity.User;
import pers.clare.post.data.repository.PostRepository;
import pers.clare.post.service.UserService;
import pers.clare.post.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"spring.profiles.active=test"}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PostControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserService userService;

    String getHost() {
        return "http://localhost:" + port;
    }

    HttpHeaders login(Long id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(getHost() + "/public/login"
                , new HttpEntity<>("id=" + id, headers)
                , String.class
        );
        List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
        if (cookies == null) return headers;
        for (String cookie : cookies) {
            if (cookie.startsWith("JSESSIONID")) {
                headers.set(HttpHeaders.COOKIE, cookie.split(";")[0]);
                break;
            }
        }

        return headers;
    }

    @Test
    @Order(1)
    void findAll() throws IOException {
        User user = userService.insert("test");
        HttpHeaders headers = login(user.getId());

        int count = 10;
        for (int i = 0; i < count; i++) {
            String message = String.valueOf(new Random().nextInt(100));
            restTemplate.postForEntity(getHost() + "/post"
                    , new HttpEntity<>("message=" + message, headers)
                    , String.class
            );
        }

        ResultData<List<Post>> response;
        List<Post> all = new ArrayList<>();
        do {
            StringBuilder url = new StringBuilder(getHost() + "/post?size=" + (new Random().nextInt(3) + 1));
            if (all.size() > 0) {
                Post post = all.get(all.size() - 1);
                url.append("&prevTime=").append(post.getTime())
                        .append("&prevUserId=").append(post.getUserId())
                ;
            }
            System.out.println(url);

            ResponseEntity<String> responseEntity = restTemplate.exchange(url.toString(), HttpMethod.GET, new HttpEntity(headers), String.class);
            System.out.println(responseEntity.getBody());
            response = JsonUtil.decode(responseEntity.getBody(), new TypeReference<ResultData<List<Post>>>() {
            });
            all.addAll(response.getData());
        } while (response.getData().size() > 0);
        assertEquals(count, all.size());
    }

    @Test
    @Order(2)
    void insert() throws IOException {
        User user = userService.insert("test");
        HttpHeaders headers = login(user.getId());

        String message = String.valueOf(new Random().nextInt(100));
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(getHost() + "/post"
                , new HttpEntity<>("message=" + message, headers)
                , String.class
        );

        assertNotNull(responseEntity.getBody());

        ResultData<Post> response = JsonUtil.decode(responseEntity.getBody(), new TypeReference<ResultData<Post>>() {
        });

        assertEquals(user.getId(), response.getData().getUserId());
        assertEquals(message, response.getData().getMessage());
    }


}
