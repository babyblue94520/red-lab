package pers.clare.post.service;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.clare.post.data.entity.User;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"spring.profiles.active=test"}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    void insert() {
        User user = userService.insert("test");
        assertNotNull(user.getId());
    }

    @Test
    @Order(2)
    void find() {
        User user = userService.insert("test2");
        assertNotNull(userService.find(user.getId()));
    }

    @Test
    @Order(3)
    void findAll() {
        assertTrue(userService.findAll().size()>0);
    }
}
