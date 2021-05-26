package pers.clare.post.data.sql;

import org.springframework.stereotype.Repository;
import pers.clare.hisql.annotation.HiSql;
import pers.clare.hisql.repository.SQLRepository;
import pers.clare.post.data.entity.Post;

import java.util.List;

@Repository
public interface PostSqlRepository extends SQLRepository {

    @HiSql("select * from post order by time desc,user_id desc limit :size")
    List<Post> findAll(int size);

    @HiSql("select * from post where time < :time or (time=:time and user_id < :userId) order by time desc,user_id desc limit :size")
    List<Post> findAll(int size, long time, long userId);
}
