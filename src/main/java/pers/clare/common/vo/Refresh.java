package pers.clare.common.vo;

import lombok.Getter;
import pers.clare.post.data.entity.User;

import java.util.List;

@Getter
public class Refresh {
    private User user;
    private List<User> users;

    public Refresh user(User user) {
        this.user = user;
        return this;
    }
    public Refresh users(List<User> users) {
        this.users = users;
        return this;
    }
}
