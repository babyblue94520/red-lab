package pers.clare.post.data.sql;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pers.clare.hisql.annotation.HiSql;
import pers.clare.hisql.repository.SQLRepository;
import pers.clare.post.data.entity.Post;

import java.util.List;

@Repository
public interface PostSqlRepository extends SQLRepository {

    @HiSql("select * from post p order by p.time desc limit :size")
    List<Post> findAll(int size);

    @HiSql("select * from Post p where p.time <= :time and p.userId != :userId order by p.time desc limit :size")
    List<Post> findAll(int size, long time, long userId);
}
