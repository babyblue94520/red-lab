package pers.clare.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.clare.post.auth.AuthAware;
import pers.clare.post.data.entity.User;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public User login(long id){
        User user = userService.find(id);
        if (user == null)  throw new IllegalArgumentException(String.format("User %d not exist", id));
        AuthAware.setUserId(id);
        return user;
    }

    public User current(){
        return userService.find(AuthAware.getUserId());
    }

    public User currentAndCheck(){
        return userService.find(AuthAware.getUserIdAndCheck());
    }
}
