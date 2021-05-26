package pers.clare.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
