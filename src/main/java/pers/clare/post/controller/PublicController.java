package pers.clare.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.clare.common.result.ResultHolder;
import pers.clare.common.result.ResultRefresh;
import pers.clare.common.vo.Refresh;
import pers.clare.post.data.entity.User;
import pers.clare.post.service.AuthService;
import pers.clare.post.service.UserService;

import java.util.List;

@RestController
@RequestMapping("public")
public class PublicController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @GetMapping("init")
    public ResultRefresh<List<User>> init() {
        return ResultHolder.out(new Refresh()
                .user(authService.current())
                .users(userService.findAll()));
    }

    @PostMapping("login")
    public ResultRefresh<List<User>> login(
            Long id
    ) {
        return ResultHolder.out(new Refresh().user(authService.login(id)));
    }
}
