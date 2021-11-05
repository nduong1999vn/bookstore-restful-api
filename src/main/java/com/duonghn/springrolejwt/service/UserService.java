package com.duonghn.springrolejwt.service;

import java.util.List;
import java.util.Optional;

import com.duonghn.springrolejwt.model.User;
import com.duonghn.springrolejwt.model.UserDto;

public interface UserService {
    User save(UserDto user);
    List<User> findAll();
    User findOne(String username);
    Optional<User> findById(long id);
    String deleteById(long id);
}
