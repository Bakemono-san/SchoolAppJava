package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.FonctionEntity;
import org.SchoolApp.Datas.Entity.RoleEntity;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Repository.FonctionRepository;
import org.SchoolApp.Datas.Repository.RoleRepository;
import org.SchoolApp.Datas.Repository.UserRepository;
import org.SchoolApp.Web.Dtos.Request.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FonctionRepository fonctionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       FonctionRepository fonctionRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.fonctionRepository = fonctionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserEntity createUser(UserRequestDto userRequestDto) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.findByEmail(userRequestDto.getEmail()) != null) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        // Récupérer le rôle
        RoleEntity role = roleRepository.findById(userRequestDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé avec l'ID : " + userRequestDto.getRoleId()));

        // Récupérer la fonction (si fournie)
        FonctionEntity fonction = null;
        if (userRequestDto.getFonctionId() != null) {
            fonction = fonctionRepository.findById(userRequestDto.getFonctionId())
                    .orElseThrow(() -> new RuntimeException("Fonction non trouvée avec l'ID : " + userRequestDto.getFonctionId()));
        }

        // Créer le nouvel utilisateur
        UserEntity newUser = new UserEntity();
        newUser.setNom(userRequestDto.getNom());
        newUser.setPrenom(userRequestDto.getPrenom());
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword())); // Hachage du mot de passe
        newUser.setAdresse(userRequestDto.getAdresse());
        newUser.setTelephone(userRequestDto.getTelephone());
        newUser.setRole(role);
        newUser.setFonction(fonction);
        newUser.setStatus(userRequestDto.getStatus()); // Si vous avez un champ status dans UserRequestDto
        newUser.setPhoto(userRequestDto.getPhoto()); // Si vous avez un champ photo dans UserRequestDto

        return userRepository.save(newUser);
    }

    public List<UserEntity> listUsers(String role) {
        return (role == null) ? userRepository.findAll() : userRepository.findByRoleLibelle(role);
    }

    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public UserEntity updateUser(Long id, UserRequestDto userRequestDto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Mettre à jour les champs de base
        user.setNom(userRequestDto.getNom());
        user.setPrenom(userRequestDto.getPrenom());
        user.setAdresse(userRequestDto.getAdresse());
        user.setTelephone(userRequestDto.getTelephone());

        // Mettre à jour le rôle si fourni
        if (userRequestDto.getRoleId() != null) {
            RoleEntity role = roleRepository.findById(userRequestDto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Rôle non trouvé avec l'ID : " + userRequestDto.getRoleId()));
            user.setRole(role);
        }

        // Mettre à jour la fonction si fournie
        if (userRequestDto.getFonctionId() != null) {
            FonctionEntity fonction = fonctionRepository.findById(userRequestDto.getFonctionId())
                    .orElseThrow(() -> new RuntimeException("Fonction non trouvée avec l'ID : " + userRequestDto.getFonctionId()));
            user.setFonction(fonction);
        }

        // Mettre à jour le statut si fourni
        if (userRequestDto.getStatus() != null) {
            user.setStatus(userRequestDto.getStatus());
        }

        // Mettre à jour la photo si fournie
        if (userRequestDto.getPhoto() != null) {
            user.setPhoto(userRequestDto.getPhoto());
        }

        return userRepository.save(user);
    }
}
