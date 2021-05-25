package pers.clare.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.clare.common.result.ResultData;
import pers.clare.common.result.ResultHolder;
import pers.clare.post.data.entity.Post;
import pers.clare.post.service.PostService;

import java.util.List;

@RestController
@RequestMapping("post")
public class PostController {

    @Autowired
    private PostService postService;


    @GetMapping
    public ResultData<List<Post>> findAll(
            int size
            , @RequestParam(required = false) Long prevTime
            , @RequestParam(required = false) Long prevUserId
    ) {
        return ResultHolder.out(postService.findAll(size, prevTime, prevUserId));
    }

    @PostMapping
    public ResultData<Post> post(String message) {
        return ResultHolder.out(postService.insert(message));
    }

}
