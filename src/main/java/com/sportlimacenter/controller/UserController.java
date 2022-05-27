package com.sportlimacenter.controller;

import com.sportlimacenter.model.request.CreateAttendanceRequest;
import com.sportlimacenter.model.request.LoginRequest;
import com.sportlimacenter.model.request.RegisterUserRequest;
import com.sportlimacenter.model.response.LoginResponse;
import com.sportlimacenter.model.response.UserResponse;
import com.sportlimacenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping(value = "/branch/{branchId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<UserResponse> getUsersByBranchId(@PathVariable(name = "branchId") String branchId) {
        return userService.getUsersByBranchId(branchId);
    }

    @GetMapping(value = "/dni/{dni}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponse> getUserByDni(@PathVariable(name = "dni") String dni) {
        return new ResponseEntity<>(userService.getUserByDni(dni), HttpStatus.OK);
    }





}
