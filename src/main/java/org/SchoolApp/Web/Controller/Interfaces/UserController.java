package org.SchoolApp.Web.Controller.Interfaces;


import org.SchoolApp.Web.Dtos.Request.UserRequestDto;
import org.SchoolApp.Web.Dtos.Request.UserUpdateRequestDto;
import org.SchoolApp.Web.Dtos.Response.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserController {
    ResponseEntity<?> createUser(UserRequestDto userRequestDto, MultipartFile excelFile);

    ResponseEntity<List<UserResponseDto>> getAllUsers(Long roleId);

    ResponseEntity<UserResponseDto> updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto);
}