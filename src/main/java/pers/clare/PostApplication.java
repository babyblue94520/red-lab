package pers.clare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pers.clare.hisql.annotation.EnableHiSql;
import pers.clare.post.data.ExtendedRepositoryImpl;

@EnableHiSql
@EnableCaching
@EnableJpaRepositories(
        repositoryBaseClass = ExtendedRepositoryImpl.class
)
@SpringBootApplication
public class PostApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostApplication.class, args);
    }
}
