package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Entity.Role;
import org.SchoolApp.Datas.Enums.StatusEnum;
import org.SchoolApp.Datas.Repository.RoleRepository;
import org.SchoolApp.Datas.Repository.UserRepository;
import org.SchoolApp.Services.Interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        user.setStatus(StatusEnum.ACTIF);

//
//        // Chiffrer le mot de passe
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encryptedPassword = passwordEncoder.encode(user.getPassword());
//        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }

    public List<UserEntity> importUsersFromExcel(InputStream excelFile) throws Exception {
        Role apprenantRole = roleRepository.findByLibelle("Apprenant");
        if (apprenantRole == null) {
            throw new IllegalArgumentException("Le rôle 'Apprenant' n'existe pas.");
        }

        List<UserEntity> users = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;

            UserEntity user = new UserEntity();
            user.setNom(row.getCell(0).getStringCellValue());
            user.setPrenom(row.getCell(1).getStringCellValue());
            user.setEmail(row.getCell(2).getStringCellValue());
            user.setTelephone(row.getCell(3).getStringCellValue());
            user.setPhoto(row.getCell(4).getStringCellValue());
            user.setPassword("default_password");
            user.setRole(apprenantRole);
            user.setStatus(StatusEnum.ACTIF);
            users.add(user);
        }

        workbook.close();
        return userRepository.saveAll(users);
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

    @Override
    public List<UserEntity> getUsersByRoleId(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Le rôle spécifié n'existe pas."));
        return userRepository.findByRole(role);
    }

    // Méthode pour trouver un utilisateur par ID
    @Override
    public UserEntity findUserById(Long id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);
        return userOptional.orElse(null); // Renvoie l'utilisateur ou null s'il n'existe pas
    }

}