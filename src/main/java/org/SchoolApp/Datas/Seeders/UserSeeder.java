package org.SchoolApp.Datas.Seeders;

import jakarta.annotation.PostConstruct;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Entity.RoleEntity;
import org.SchoolApp.Datas.Entity.FonctionEntity;
import org.SchoolApp.Datas.Enums.StatusEnum;
import org.SchoolApp.Datas.Repository.UserRepository;
import org.SchoolApp.Datas.Repository.RoleRepository;
import org.SchoolApp.Datas.Repository.FonctionRepository;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DependsOn({"roleSeeder", "fonctionSeeder"})
public class UserSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FonctionRepository fonctionRepository;

    public UserSeeder(UserRepository userRepository, RoleRepository roleRepository, FonctionRepository fonctionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.fonctionRepository = fonctionRepository;
    }

    @PostConstruct
    public void seedUsers() {
        if (userRepository.count() == 0) { // Only seed if no users exist
            List<RoleEntity> roles = roleRepository.findAll();
            List<FonctionEntity> fonctions = fonctionRepository.findAll();

            UserEntity user1 = createUser("John", "Doe", "1234 Drive Lane", "1234567890", "john.doe@example.com", "password123", roles.get(0), fonctions.get(0));
            UserEntity user2 = createUser("Jane", "Doe", "1234 Park Ave", "0987654321", "jane.doe@example.com", "securepassword", roles.get(1), fonctions.get(1));

            userRepository.save(user1);
            userRepository.save(user2);
        }
    }

    private UserEntity createUser(String nom, String prenom, String adresse, String telephone, String email, String password, RoleEntity role, FonctionEntity fonction) {
        UserEntity newUser = new UserEntity();
        newUser.setNom(nom);
        newUser.setPrenom(prenom);
        newUser.setAdresse(adresse);
        newUser.setTelephone(telephone);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(role);
        newUser.setFonction(fonction);
        newUser.setStatus(StatusEnum.ACTIF);
        return newUser;
    }
}
