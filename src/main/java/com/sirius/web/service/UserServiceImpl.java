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
    public User findUserByEmail(String email) {
        return userRepository.findById(email).orElse(null);
    }

    @Override
    public User updateUser(String email, User user) {
        User existingUser = userRepository.findById(email).orElse(null);
        BeanUtils.copyProperties(user, existingUser);
        return userRepository.save(existingUser);
    }

    @Override
    public User deleteUser(String email, User user) {
        final User dbuser = this.authenticate(user.getEmail(), user.getPassword());
        if(dbuser== null || dbuser.getEmail().equals(email) == false){
            throw new RuntimeException("Wrong User");
        }
        userRepository.deleteById(email);
        return user;
    }

    @Override
    public User authenticate(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
