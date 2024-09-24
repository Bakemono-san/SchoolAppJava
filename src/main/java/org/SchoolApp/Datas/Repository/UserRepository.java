package org.SchoolApp.Datas.Repository;

import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Entity.Role;
import org.SchoolApp.Datas.Enums.StatusEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends SoftDeleteRepository<UserEntity, Long> {

    // Trouver un utilisateur par email
    UserEntity findByEmail(String email);

    // Trouver des utilisateurs par rôle
    List<UserEntity> findByRole(Role role);

    // Trouver des utilisateurs par statut
    List<UserEntity> findByStatus(StatusEnum status);

    // Trouver des utilisateurs par rôle et statut
    List<UserEntity> findByRoleAndStatus(Role role, StatusEnum status);

    // Trouver tous les utilisateurs non supprimés
    List<UserEntity> findByDeletedFalse();
}
