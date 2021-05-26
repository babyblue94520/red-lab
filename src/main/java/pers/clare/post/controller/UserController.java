package pers.clare.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pers.clare.common.result.ResultHolder;
import pers.clare.common.result.ResultRefresh;
import pers.clare.common.vo.Refresh;
import pers.clare.post.data.entity.User;
import pers.clare.post.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResultRefresh<User> insert(String name) {
        return ResultHolder.out(userService.insert(name)
                , new Refresh().users(userService.findAll())
        );
    }

    @PatchMapping
    public ResultRefresh<User> update(User user) {
        return ResultHolder.out(userService.update(user)
                , new Refresh().users(userService.findAll())
        );
    }

    @DeleteMapping
    public ResultRefresh<User> delete(Long id) {
        userService.delete(id);
        return ResultHolder.out(new Refresh().users(userService.findAll()));
    }

}
