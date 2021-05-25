package pers.clare.post.data.repository;

import org.springframework.data.jpa.repository.Query;
import pers.clare.post.data.ExtendedRepository;
import pers.clare.post.data.entity.Post;
import pers.clare.post.data.entity.PostPK;

import java.util.List;

public interface PostRepository extends ExtendedRepository<Post, PostPK> {
}
