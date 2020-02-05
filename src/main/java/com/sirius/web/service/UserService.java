package com.sirius.web.service;

import com.sirius.web.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User createUser(User user);

    User findUserByEmail(String email);

    User updateUser(String email, User user);

    User deleteUser(String email, User user);

    User authenticate(String email, String password);

}
