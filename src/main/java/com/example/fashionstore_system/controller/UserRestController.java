package com.example.fashionstore_system.controller;

import com.example.fashionstore_system.dto.UserDto;
import com.example.fashionstore_system.dto.UserInput;
import com.example.fashionstore_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/check_username")
    public ResponseEntity<UserDto> getUserByUserName(@RequestBody UserInput userInput) {
        UserDto userDto = userService.getUserByUserName(userInput.getUserName());
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    @PostMapping("/check_email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestBody UserInput userInput) {
        UserDto userDto = userService.getUserByEmail(userInput.getEmail());
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

}

