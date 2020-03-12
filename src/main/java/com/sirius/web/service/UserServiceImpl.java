package com.sirius.web.service;

import com.sirius.web.model.User;
import com.sirius.web.repository.UserRepository;
import com.sirius.web.utils.HashingForPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
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
        boolean dbUserIsExist = exists(email);
        if(dbUserIsExist) {
            userRepository.deleteById(email);
            return true;
        }
        return false;
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
    public boolean authenticate(String email, String password) {
        try {
            String hashedPassword = HashingForPassword.hash(password);
            User dbUser = userRepository.findByEmailAndPassword(email, hashedPassword);
            if (dbUser != null) {
                return true;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
