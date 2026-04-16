package com.privatekitchen.admin.service;

import com.privatekitchen.admin.entity.User;

import java.util.List;

public interface UserService {

    List<User> listUsers();

    User saveUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
