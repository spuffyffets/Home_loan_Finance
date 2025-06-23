package com.sit.homeloan.serviceimpl;

import com.sit.homeloan.model.Role;
import com.sit.homeloan.model.User;
import com.sit.homeloan.repository.UserRepository;
import com.sit.homeloan.service.UserService;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

//    @Override
//    public String registerUser(User user) {
//        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//            return "User already exists!";
//        }
//        userRepository.save(user);
//        return "User registered as " + user.getRole();
//    }

    @Override
    public String registerUser(User user) {
        
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "User already exists!";
        }

        
        if (user.getRole() == Role.LOAN_OFFICER ||
            user.getRole() == Role.CREDIT_MANAGER ||
            user.getRole() == Role.DISBURSEMENT_MANAGER) {

            boolean roleExists = userRepository.existsByRole(user.getRole());
            if (roleExists) {
                return "A user with role " + user.getRole() + " is already registered.";
            }
        }

        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        
        userRepository.save(user);
        return "User registered as " + user.getRole();
    }
 
    @Override
    public User login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new RuntimeException("Incorrect password");
        }

        return user;
    }


}