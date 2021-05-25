package pers.clare.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pers.clare.post.data.entity.User;
import pers.clare.post.data.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Cacheable(
            cacheNames = "users"
            , key = "'all'"
            , unless = "#result==null"
    )
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Cacheable(
            cacheNames = "user"
            , key = "#id"
            , condition = "#id!=null"
            , unless = "#result==null"
    )
    public User find(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id).get();
    }

    @CacheEvict(cacheNames = "users", key = "'all'")
    public User insert(String name) {
        User user = new User();
        user.setName(name);
        return userRepository.insert(user);
    }

}
