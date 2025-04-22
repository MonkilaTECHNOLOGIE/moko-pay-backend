package com.monkilatech.madeinrdc.services;

import java.util.List;
import java.util.UUID;

import com.monkilatech.madeinrdc.models.User;

public interface UserService {
    User saveUser(User user) throws Exception;

    List<User> fetchUserList() throws Exception;

    User getUserById(UUID userId) throws Exception;

    // User getUserByPhone(String phone) throws Exception;

    User updateUser(User user) throws Exception;

    User updatePassword(UUID id, String password) throws Exception;

    Boolean deleteUserById(UUID userId) throws Exception;
}
