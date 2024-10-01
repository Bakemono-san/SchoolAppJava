package org.SchoolApp.Datas.Seeders;

import jakarta.annotation.PostConstruct;
import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Datas.Entity.PromoEntity;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Entity.RoleEntity;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Repository.ApprenantRepository;
import org.SchoolApp.Datas.Repository.PromoRepository;
import org.SchoolApp.Datas.Repository.ReferentielRepository;
import org.SchoolApp.Datas.Repository.RoleRepository;
import org.SchoolApp.Datas.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn({"roleSeeder", "referentielSeeder", "userSeeder", "promoSeeder"})
public class ApprenantSeeder {

    private final ApprenantRepository apprenantRepository;
    private final UserRepository userRepository;
    private final ReferentielRepository referentielRepository;
    private final RoleRepository roleRepository;
    private final PromoRepository promoRepository;

    @Autowired
    public ApprenantSeeder(ApprenantRepository apprenantRepository, UserRepository userRepository,
                           ReferentielRepository referentielRepository, RoleRepository roleRepository,
                           PromoRepository promoRepository) {
        this.apprenantRepository = apprenantRepository;
        this.userRepository = userRepository;
        this.referentielRepository = referentielRepository;
        this.roleRepository = roleRepository;
        this.promoRepository = promoRepository;
    }

    @PostConstruct
    public void seedApprenants() {
        if (apprenantRepository.count() == 0) {
            RoleEntity roleApprenant = roleRepository.findByLibelle("Apprenant");
            ReferentielEntity referentiel = referentielRepository.findById(1L).orElse(null);
            PromoEntity promo = promoRepository.findById(1L).orElse(null);

            // Ensure users are created with all necessary fields to prevent `DataIntegrityViolationException`
            createUserAndApprenant("John", "Doe", "1234 Drive Lane", "12344567890", "john.doed@example.com", "password123", roleApprenant, referentiel, promo);
            createUserAndApprenant("Jane", "Doe", "1234 Park Ave", "09876454321", "jane.doghe@example.com", "securepassword", roleApprenant, referentiel, promo);
        }
    }

    private void createUserAndApprenant(String nom, String prenom, String adresse, String telephone, String email, String password, RoleEntity role, ReferentielEntity referentiel, PromoEntity promo) {
        UserEntity user = new UserEntity();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setAdresse(adresse);
        user.setTelephone(telephone);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        userRepository.save(user);

        ApprenantEntity apprenant = new ApprenantEntity();
        apprenant.setNomTuteur("Tutor " + nom);
        apprenant.setPrenomTuteur("Tutor " + prenom);
        apprenant.setContactTuteur("Contact Info");
        apprenant.setUser(user);
        apprenant.setReferentiel(referentiel);
        apprenant.setPromo(promo); // Set the associated promo
        apprenantRepository.save(apprenant);
    }
}
