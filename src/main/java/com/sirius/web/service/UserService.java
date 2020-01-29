package com.sirius.web.service;

import com.sirius.web.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User createUser(User user);

    User findUserById(String id);

    User updateUser(String id, User user);

    User deleteUser(String id, User user);

    User authenticate(String id, String password);

}
