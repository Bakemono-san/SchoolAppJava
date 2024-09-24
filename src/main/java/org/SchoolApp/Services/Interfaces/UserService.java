package org.SchoolApp.Services.Interfaces;

import org.SchoolApp.Datas.Entity.UserEntity;

import java.io.InputStream;
import java.util.List;

public interface UserService {
    UserEntity createUser(UserEntity user);
    List<UserEntity> getAllActiveUsers();
    UserEntity updateUser(Long id, UserEntity updatedUser);
    List<UserEntity> getUsersByRole(String libelle);
    List<UserEntity> importUsersFromExcel(InputStream excelFile) throws Exception;
    List<UserEntity> getUsersByRoleId(Long roleId);
    UserEntity findUserById(Long id);
}