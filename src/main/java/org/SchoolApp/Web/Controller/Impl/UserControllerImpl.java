package org.SchoolApp.Web.Controller.Impl;

import org.SchoolApp.Datas.Enums.StatusEnum;
import org.SchoolApp.Exceptions.BadRequestException;
import org.SchoolApp.Exceptions.NotFoundException;
import org.SchoolApp.Web.Controller.Interfaces.UserController;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Entity.Role;
import org.SchoolApp.Web.Dtos.Request.UserRequestDto;
import org.SchoolApp.Web.Dtos.Request.UserUpdateRequestDto;
import org.SchoolApp.Web.Dtos.Response.UserResponseDto;
import org.SchoolApp.Services.Interfaces.UserService;
import org.SchoolApp.Datas.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserControllerImpl(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    @PostMapping
    public ResponseEntity<?> createUser(
            @Validated @RequestBody UserRequestDto userRequestDto, @RequestParam(required = false) MultipartFile excelFile
    ) {
        try {
            if (excelFile != null) {
                InputStream inputStream = excelFile.getInputStream();
                List<UserEntity> importedUsers = userService.importUsersFromExcel(inputStream);
                return new ResponseEntity<>(importedUsers, HttpStatus.CREATED);
            } else {
                Role role = roleRepository.findById(userRequestDto.getRoleId())
                        .orElseThrow(() -> new NotFoundException("Le rôle spécifié n'existe pas."));

                UserEntity userEntity = new UserEntity();
                userEntity.setNom(userRequestDto.getNom());
                userEntity.setPrenom(userRequestDto.getPrenom());
                userEntity.setTelephone(userRequestDto.getTelephone());
                userEntity.setEmail(userRequestDto.getEmail());
                userEntity.setPassword(userRequestDto.getPassword());
                userEntity.setPhoto(userRequestDto.getPhoto());
                userEntity.setRole(role);
                userEntity.setStatus(StatusEnum.ACTIF);

                UserEntity createdUser = userService.createUser(userEntity);
                return new ResponseEntity<>(mapToResponseDto(createdUser), HttpStatus.CREATED);
            }
        } catch (Exception e) {
            throw new BadRequestException("Erreur lors de la création de l'utilisateur : " + e.getMessage());
        }
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(@RequestParam(required = false) Long roleId) {
        List<UserEntity> users;

        if (roleId != null) {
            users = userService.getUsersByRoleId(roleId);
        } else {
            users = userService.getAllActiveUsers();
        }

        List<UserResponseDto> responseDtos = users.stream().map(this::mapToResponseDto).collect(Collectors.toList());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Validated @RequestBody UserUpdateRequestDto userUpdateRequestDto
    ) {
        // Rechercher l'utilisateur existant
        UserEntity existingUser = userService.findUserById(id);
        if (existingUser == null) {
            throw new NotFoundException("Utilisateur avec l'ID " + id + " non trouvé");
        }

        // Mettre à jour les champs non nuls dans l'entité utilisateur existante
        UserEntity updatedUser = mapToUserEntity(existingUser, userUpdateRequestDto);

        // Sauvegarder les modifications
        updatedUser = userService.updateUser(id, updatedUser);

        return new ResponseEntity<>(mapToResponseDto(updatedUser), HttpStatus.OK);
    }

    private UserResponseDto mapToResponseDto(UserEntity user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setNom(user.getNom());
        responseDto.setPrenom(user.getPrenom());
        responseDto.setEmail(user.getEmail());
        responseDto.setTelephone(user.getTelephone());
        responseDto.setPhoto(user.getPhoto());
        responseDto.setRole(user.getRole().getLibelle());
        responseDto.setDeleted(user.isDeleted());
        return responseDto;
    }

    private UserEntity mapToUserEntity(UserEntity existingUser, UserUpdateRequestDto userUpdateRequestDto) {
        if (userUpdateRequestDto.getNom() != null && !userUpdateRequestDto.getNom().isEmpty()) {
            existingUser.setNom(userUpdateRequestDto.getNom());
        }

        if (userUpdateRequestDto.getPrenom() != null && !userUpdateRequestDto.getPrenom().isEmpty()) {
            existingUser.setPrenom(userUpdateRequestDto.getPrenom());
        }

        if (userUpdateRequestDto.getTelephone() != null && !userUpdateRequestDto.getTelephone().isEmpty()) {
            existingUser.setTelephone(userUpdateRequestDto.getTelephone());
        }

        if (userUpdateRequestDto.getEmail() != null && !userUpdateRequestDto.getEmail().isEmpty()) {
            existingUser.setEmail(userUpdateRequestDto.getEmail());
        }

        if (userUpdateRequestDto.getPhoto() != null && !userUpdateRequestDto.getPhoto().isEmpty()) {
            existingUser.setPhoto(userUpdateRequestDto.getPhoto());
        }

        if (userUpdateRequestDto.getPassword() != null && !userUpdateRequestDto.getPassword().isEmpty()) {
            existingUser.setPassword(userUpdateRequestDto.getPassword());
        }

        return existingUser;
    }
}
