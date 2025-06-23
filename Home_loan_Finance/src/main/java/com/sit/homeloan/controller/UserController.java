//package com.sit.homeloan.controller;
//
//import com.sit.homeloan.model.User;
//import com.sit.homeloan.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/user")
//@CrossOrigin("*")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody User user) {
//        String result = userService.registerUser(user);
//        return ResponseEntity.ok(result);
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User loginData) {
//        try {
//            User user = userService.login(loginData.getEmail(), loginData.getPassword());
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//}


package com.sit.homeloan.controller;

import com.sit.homeloan.model.User;
import com.sit.homeloan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    
    @PostMapping("/login")
    public Object loginUser(@RequestBody User loginData) {
        try {
            return userService.login(loginData.getEmail(), loginData.getPassword());
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}

















