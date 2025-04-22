package com.monkila_tech.mokopay_backend.services;

import java.util.List;

import com.monkila_tech.mokopay_backend.models.User;




public interface UserService {
    User saveUser(User user) throws Exception;

    List<User> fetchUserList() throws Exception;

    User getUserById(Long userId) throws Exception;

    // User getUserByPhone(String phone) throws Exception;

    User updateUser(User user) throws Exception;

    User updatePassword(Long id, String password) throws Exception;

    Boolean deleteUserById(Long userId) throws Exception;
}
