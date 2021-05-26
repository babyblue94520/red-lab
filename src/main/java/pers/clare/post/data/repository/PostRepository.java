package pers.clare.post.data.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pers.clare.post.data.ExtendedRepository;
import pers.clare.post.data.entity.Post;
import pers.clare.post.data.entity.PostPK;


public interface PostRepository extends ExtendedRepository<Post, PostPK> {

    @Modifying
    @Transactional
    @Query("update Post set message=:message where time=:time and userId=:userId")
    int update(Long time,Long userId,String message);
}
