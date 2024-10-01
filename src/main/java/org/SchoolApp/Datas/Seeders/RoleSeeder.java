package org.SchoolApp.Datas.Seeders;

import jakarta.annotation.PostConstruct;
import org.SchoolApp.Datas.Entity.RoleEntity;
import org.SchoolApp.Datas.Repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleSeeder {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void seedRoles() {
        List<String> roles = List.of("Admin", "Coach", "Manager", "CM", "Apprenant");

        for (String libelle : roles) {
            if (roleRepository.findByLibelle(libelle) == null) {  // Vérifie si le rôle existe déjà
                RoleEntity role = new RoleEntity();
                role.setLibelle(libelle);
                roleRepository.save(role);
            }
        }
    }
}
