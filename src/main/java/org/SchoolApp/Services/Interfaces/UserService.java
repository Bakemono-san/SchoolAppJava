package org.SchoolApp.Services.Interfaces;

import org.SchoolApp.Datas.Entity.UserEntity;
import java.util.List;

public interface UserService {
    UserEntity createUser(UserEntity user);
    List<UserEntity> getAllActiveUsers();
    UserEntity updateUser(Long id, UserEntity updatedUser);
    List<UserEntity> getUsersByRole(String libelle);
}
