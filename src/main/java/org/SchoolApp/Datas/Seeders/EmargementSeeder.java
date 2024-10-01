package org.SchoolApp.Datas.Seeders;

import jakarta.annotation.PostConstruct;
import org.SchoolApp.Datas.Entity.EmargementEntity;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Repository.EmargementRepository;
import org.SchoolApp.Datas.Repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class EmargementSeeder {

    private final EmargementRepository emargementRepository;
    private final UserRepository userRepository;

    public EmargementSeeder(EmargementRepository emargementRepository, UserRepository userRepository) {
        this.emargementRepository = emargementRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void seedEmargements() {
        if (emargementRepository.count() == 0) {
            UserEntity user = userRepository.findById(1L).orElse(null);
            if (user != null) {
                EmargementEntity emargement = new EmargementEntity();
                emargement.setDate(LocalDate.now());
                emargement.setEntree(LocalTime.of(9, 0));
                emargement.setSortie(LocalTime.of(17, 0));
                emargement.setUser(user);
                emargementRepository.save(emargement);
            }
        }
    }
}
