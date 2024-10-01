package org.SchoolApp.Web.Controller.Impl;

import org.SchoolApp.Datas.Entity.UserEntity;


import org.SchoolApp.Services.Impl.UserService;
import org.SchoolApp.Web.Dtos.Request.UserRequestDto;
import org.SchoolApp.Web.Dtos.Response.UserResponseDto;
import org.SchoolApp.Web.Dtos.Mapper.UserMapper;
import org.odc.core.services.AuthService;
import org.odc.core.Web.Dtos.Request.AuthRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(AuthService authService, UserService userService, UserMapper userMapper) {
        this.authService = authService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Endpoint for user login
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        String token = authService.authenticateAndGenerateToken(authRequest);
        return ResponseEntity.ok(token);
    }

    // Endpoint to create a new user
    @PostMapping("/users")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        UserEntity newUser = userService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(newUser));
    }

    // Endpoint to list users, optionally filtered by role
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> listUsers(@RequestParam(required = false) String role) {
        List<UserEntity> users = userService.listUsers(role);
        return ResponseEntity.ok(users.stream().map(userMapper::toDto).collect(Collectors.toList()));
    }

    // Endpoint to update a user by id
    @PatchMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        UserEntity updatedUser = userService.updateUser(id, userRequestDto);
        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }
}
