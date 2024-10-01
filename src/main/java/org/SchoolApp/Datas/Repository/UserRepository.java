package org.SchoolApp.Datas.Repository;

import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Entity.RoleEntity;
import org.SchoolApp.Datas.Enums.StatusEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends SoftDeleteRepository<UserEntity, Long> {

    // Trouver un utilisateur par email
    UserEntity findByEmail(String email);

    // Trouver des utilisateurs par libellé de rôle
    List<UserEntity> findByRoleLibelle(String role);

    // Trouver des utilisateurs par statut
    List<UserEntity> findByStatus(StatusEnum status);

    // Trouver des utilisateurs par rôle et statut
    List<UserEntity> findByRoleAndStatus(RoleEntity role, StatusEnum status);

    // Trouver tous les utilisateurs non supprimés
    List<UserEntity> findByDeletedFalse();
}
