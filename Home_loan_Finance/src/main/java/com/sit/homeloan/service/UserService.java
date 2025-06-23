package com.sit.homeloan.service;

import com.sit.homeloan.model.User;

public interface UserService {
    String registerUser(User user);
    User login(String email, String password);
}