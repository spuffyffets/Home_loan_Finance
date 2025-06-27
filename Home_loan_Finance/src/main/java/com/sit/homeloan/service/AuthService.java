package com.sit.homeloan.service;

import com.sit.homeloan.model.User;

public interface AuthService {
    String registerUser(User user);
    String loginUser(String email, String password);
}
