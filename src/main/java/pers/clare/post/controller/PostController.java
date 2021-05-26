package pers.clare.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResultData<Post> insert(String message) {
        return ResultHolder.out(postService.insert(message));
    }

    @PatchMapping
    public ResultData<Integer> update(
            Long time
            , Long userId
            , String message
    ) {
        return ResultHolder.out(postService.update(time, userId, message));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            Long time
            , Long userId
    ) {
        postService.delete(time, userId);
    }

}
