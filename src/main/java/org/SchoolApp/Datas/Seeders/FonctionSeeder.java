package org.SchoolApp.Datas.Seeders;

import jakarta.annotation.PostConstruct;
import org.SchoolApp.Datas.Entity.FonctionEntity;
import org.SchoolApp.Datas.Repository.FonctionRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FonctionSeeder {

    private final FonctionRepository fonctionRepository;

    public FonctionSeeder(FonctionRepository fonctionRepository) {
        this.fonctionRepository = fonctionRepository;
    }

    @PostConstruct
    public void seedFonctions() {
        List<String> fonctions = List.of("Developpeur Front", "Developpeur Back", "Developpeur FullStack", "Community Manager");

        for (String libelle : fonctions) {
            if (fonctionRepository.findByLibelle(libelle) == null) {  // Vérifie si la fonction existe déjà
                FonctionEntity fonction = new FonctionEntity();
                fonction.setLibelle(libelle);
                fonction.setDescription("");  // Vous pouvez ajouter une description si nécessaire
                fonctionRepository.save(fonction);  // Sauvegarde de la fonction dans la base de données
            }
        }
    }
}
