package com.sportlimacenter.controller;

import com.sportlimacenter.model.request.LoginRequest;
import com.sportlimacenter.model.request.RegisterUserRequest;
import com.sportlimacenter.model.response.LoginResponse;
import com.sportlimacenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/login", produces = {"application/json"})
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return new ResponseEntity<>(userService.validateLogin(request.getUser(), request.getPassword()), HttpStatus.OK);
    }

    @PostMapping(value = "/register", produces = {"application/json"})
    public ResponseEntity<LoginResponse> registerUser(@RequestBody RegisterUserRequest request) {
        userService.registerUser(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
