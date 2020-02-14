package com.sirius.web.service;

import com.sirius.web.model.User;
import com.sirius.web.repository.UserRepository;
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
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(String email) {
        final User dbuser = this.authenticate(findUserByEmail(email).getEmail(), findUserByEmail(email).getPassword());
        if(dbuser == null){
            throw new RuntimeException("Wrong User");
        }
        userRepository.deleteById(email);
        return true;
    }

    @Override
    public boolean deleteAll() {
        userRepository.deleteAll();
        return true;
    }

    @Override
    public boolean exists(String email) {
        return userRepository.existsById(email);
    }

    @Override
    public User authenticate(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
}
