package com.sirius.web.service;

import com.sirius.web.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User createUser(User user);

    User findUserByEmail(String email);

    User updateUser(User user);

    boolean deleteUser(String email);

    boolean deleteAll();

    boolean exists(String email);

    User authenticate(String email, String password);

}
