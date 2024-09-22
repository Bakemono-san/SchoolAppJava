package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Entity.Role;
import org.SchoolApp.Datas.Repository.RoleRepository;
import org.SchoolApp.Datas.Repository.UserRepository;
import org.SchoolApp.Services.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        Role role = roleRepository.findByLibelle(user.getRole().getLibelle());
        if (role == null) {
            throw new IllegalArgumentException("Le rôle spécifié n'existe pas.");
        }
        user.setRole(role);

//        // Chiffrer le mot de passe
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encryptedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllActiveUsers() {
        return userRepository.findByDeletedFalse();
    }

    @Override
    public UserEntity updateUser(Long id, UserEntity updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setNom(updatedUser.getNom());
            user.setPrenom(updatedUser.getPrenom());
            user.setAdresse(updatedUser.getAdresse());
            user.setTelephone(updatedUser.getTelephone());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword()); // Gérer le chiffrement ici si nécessaire
            return userRepository.save(user);
        }).orElse(null);
    }

    @Override
    public List<UserEntity> getUsersByRole(String libelle) {
        Role role = roleRepository.findByLibelle(libelle);
        if (role == null) {
            throw new IllegalArgumentException("Le rôle spécifié n'existe pas.");
        }
        return userRepository.findByRole(role);
    }
}
