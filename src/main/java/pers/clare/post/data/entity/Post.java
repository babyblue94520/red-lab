package pers.clare.post.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Getter
@Setter
@Entity
@IdClass(PostPK.class)
public class Post {
    @Id
    private Long time;

    @Id
    private Long userId;

    private String message;

    private Integer replyCount;
}
