package com.example.demo.controller;

import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UserLoginDTO;
import com.example.demo.exception.UserIdNotExitsException;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserDTO user) {
        try {
            User savedUser = userService.signup(user);
            return new ResponseEntity<>(savedUser, HttpStatus.ACCEPTED);
        }catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/verify/otp")
    public ResponseEntity verifyOtp(String userId, String enteredOtp) {
        try {
            Boolean isVerified = userService.verifyOtp(userId, enteredOtp);
            if (isVerified) {
                return new ResponseEntity<>("Otp Verified Successfully now you can login", HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>("Entered wrong otp...", HttpStatus.NOT_FOUND);
            }
        } catch (UserIdNotExitsException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserLoginDTO userLogin) {
        User loginUser = userService.login(userLogin);
    }
}
