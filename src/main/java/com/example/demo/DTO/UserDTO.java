package com.example.demo.DTO;

import com.example.demo.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String userId;
    private String emailId;
    private String password;
    private List<Address> address;
}
