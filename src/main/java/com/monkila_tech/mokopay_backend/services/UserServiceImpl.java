package com.monkilatech.madeinrdc.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.monkilatech.madeinrdc.models.User;
import com.monkilatech.madeinrdc.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public User saveUser(User user) throws Exception {
        return userRepository.save(user);
    }

    @Override
    public List<User> fetchUserList() throws Exception {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public User getUserById(UUID userId) throws Exception {
        return userRepository.findById(userId).get();
    }

    // @Override
    // public User getUserByPhone(String phone) throws Exception {
    //     return userRepository.findByPhone(phone);
    // }

    @Override
    public Boolean deleteUserById(UUID userId) throws Exception {

        @SuppressWarnings("rawtypes")
        Optional user = this.userRepository.findById(userId);

        if (user.isEmpty())
            return false;

        this.userRepository.deleteById(userId);

        @SuppressWarnings("rawtypes")
        Optional userChecked = this.userRepository.findById(userId);

        if (userChecked.isEmpty())
            return true;
        return false;

    }

    @Override
    public User updateUser(User user) throws Exception {

        User userDB = userRepository.findById(user.getId())
                .get();

        if (Objects.nonNull(user.getUsername())
                && !"".equalsIgnoreCase(
                        user.getUsername())) {
            userDB.setUsername(
                    user.getUsername());
        }

        if (Objects.nonNull(user.getEmail())
                && !"".equalsIgnoreCase(
                        user.getEmail())) {
            userDB.setEmail(
                    user.getEmail());
        }

        if (Objects.nonNull(user.getPassword())
                && !"".equalsIgnoreCase(
                        user.getPassword())) {
            userDB.setPassword(
                    encoder.encode(user.getPassword()));
        }

        if (Objects.nonNull(user.getProfil())
                && !"".equalsIgnoreCase(
                        user.getProfil())) {
            userDB.setProfil(
                    user.getProfil());
        }

        userDB.setStatus(user.getStatus());
        userDB.setRoles(user.getRoles());

        return userRepository.save(userDB);
    }

    @Override
    public User updatePassword(UUID id, String password) throws Exception {

        User userDB = userRepository.findById(id)
                .get();

        if (Objects.nonNull(password)
                && !"".equalsIgnoreCase(
                        password)) {
            userDB.setPassword(
                    encoder.encode(password));
        }

        return userRepository.save(userDB);
    }
}
