package pers.clare.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import pers.clare.common.result.ResultData;
import pers.clare.common.result.ResultHolder;
import pers.clare.common.result.ResultRefresh;
import pers.clare.common.vo.Refresh;
import pers.clare.post.data.entity.User;
import pers.clare.post.service.UserService;

import java.util.List;

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
