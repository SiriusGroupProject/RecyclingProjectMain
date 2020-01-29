package com.sirius.web.service;

import com.sirius.web.model.User;
import com.sirius.web.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(String id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        BeanUtils.copyProperties(user, existingUser);
        return userRepository.save(existingUser);
    }

    @Override
    public User deleteUser(String id, User user) {
        final User dbuser = this.authenticate(user.getId(), user.getPassword());
        if(dbuser == null){
            throw new RuntimeException("Wrong User");
        }
        userRepository.deleteById(id);
        return user;
    }

    @Override
    public User authenticate(String id, String password) {
        return userRepository.findByUsernameAndPassword(id, password);
    }
}
