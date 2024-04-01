package com.example.demo.controller;

import com.example.demo.DTO.UserDTO;
import com.example.demo.DTO.UserLoginDTO;
import com.example.demo.exception.LoginFailedException;
import com.example.demo.exception.UserIdNotExitsException;
import com.example.demo.model.Authority;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody UserDTO user) {
        try {
            User savedUser = userService.signup(user);
            return new ResponseEntity<>(savedUser, HttpStatus.ACCEPTED);
        }catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/verify/otp")
    public ResponseEntity<String> verifyOtp(String userId, String enteredOtp) {
        try {
            boolean isVerified = userService.verifyOtp(userId, enteredOtp);
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
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO userLogin) {
        try {
            User loginUser = userService.login(userLogin);
            return new ResponseEntity<>(loginUser, HttpStatus.ACCEPTED);
        } catch (LoginFailedException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/role/{id}")
    public ResponseEntity<Object> giveRole(@PathVariable("id") String userId, @RequestBody List<Authority> roles) {
        try {
            User updatedUser = userService.giveRole(userId, roles);
            return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
        } catch (UserIdNotExitsException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
