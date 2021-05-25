package pers.clare.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import pers.clare.post.data.entity.Post;
import pers.clare.post.data.entity.User;
import pers.clare.post.data.repository.PostRepository;
import pers.clare.post.data.sql.PostSqlRepository;

import java.util.List;

@Service
public class PostService {
    private static final int retryCount = 5;

    @Autowired
    private AuthService authService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostSqlRepository postSqlRepository;

    public List<Post> findAll(
            int size
            , Long prevTime
            , Long prevUserId
    ) {
        if (prevTime == null) {
            return postSqlRepository.findAll(size);
        } else {
            return postSqlRepository.findAll(size, prevTime, prevUserId);
        }
    }

    public Post insert(String message) {
        User user = authService.currentAndCheck();
        long time = System.currentTimeMillis();
        Post post = new Post();
        post.setTime(time);
        post.setUserId(user.getId());
        post.setReplyCount(0);
        post.setMessage(message);
        return doRetryInsert(post, 0);
    }

    public Post doRetryInsert(Post post, int count) {
        try {
            return postRepository.insert(post);
        } catch (DuplicateKeyException duplicateKeyException) {
            if (count > retryCount) {
                throw duplicateKeyException;
            }
            post.setTime(post.getTime() + 1);
            return doRetryInsert(post, count + 1);
        }
    }

}
