package pers.clare.post.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
public class PostReplyPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long postTime;

    @Id
    private Long postUserId;

    @Id
    private Long time;

    @Id
    private Long userId;

    private String message;

    private Integer replyCount;
}
