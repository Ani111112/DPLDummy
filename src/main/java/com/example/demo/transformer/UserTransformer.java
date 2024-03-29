package com.example.demo.transformer;

import com.example.demo.DTO.UserDTO;
import com.example.demo.model.User;

public class UserTransformer {
    public static User UserDtoToUser(UserDTO userDTO) {
        return User.builder()
                .userId(userDTO.getUserId())
                .name(userDTO.getName())
                .admin(false)
                .emailId(userDTO.getEmailId())
                .addressList(userDTO.getAddress())
                .password(userDTO.getPassword())
                .build();
    }
}
