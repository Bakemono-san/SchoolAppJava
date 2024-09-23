package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.Role;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Enums.StatusEnum;
import org.SchoolApp.Datas.Repository.RoleRepository;
import org.SchoolApp.Datas.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // Rechercher un utilisateur par email
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Rechercher les utilisateurs par rôle
    public List<UserEntity> getUsersByRole(String libelle) {
        Role role = roleRepository.findByLibelle(libelle);
        return userRepository.findByRole(role);
    }

    // Rechercher les utilisateurs par statut
    public List<UserEntity> getUsersByStatus(StatusEnum status) {
        return userRepository.findByStatus(status);
    }

    // Rechercher les utilisateurs par rôle et statut
    public List<UserEntity> getUsersByRoleAndStatus(String libelle, StatusEnum status) {
        Role role = roleRepository.findByLibelle(libelle);
        return userRepository.findByRoleAndStatus(role, status);
    }

    // Lister tous les utilisateurs non supprimés
    //public List<UserEntity> getAllActiveUsers() {
      //  return userRepository.findByDeletedFalse();
    //}

    // Lister tous les utilisateurs (y compris ceux supprimés)
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // Afficher tous les utilisateurs avec leurs détails
    public void getAllUsersWithDetails() {
        List<UserEntity> users = userRepository.findAll();
        for (UserEntity user : users) {
            System.out.println("Utilisateur: " + user.getNom() + " " + user.getPrenom());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Téléphone: " + user.getTelephone()); // Assurez-vous que cela correspond à 'telephone'
            System.out.println("Statut: " + user.getStatus());
            System.out.println("Rôle: " + user.getRole().getLibelle());
            System.out.println("==================================");
        }
    }

}
