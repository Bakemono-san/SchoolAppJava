package org.SchoolApp.Datas.Seeders;

import jakarta.annotation.PostConstruct;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Enums.StatusReferenceEnum;
import org.SchoolApp.Datas.Repository.ReferentielRepository;
import org.springframework.stereotype.Component;

@Component
public class ReferentielSeeder {

    private final ReferentielRepository referentielRepository;

    public ReferentielSeeder(ReferentielRepository referentielRepository) {
        this.referentielRepository = referentielRepository;
    }

    @PostConstruct
    public void seedReferentiels() {
        if (referentielRepository.count() == 0) { // Only seed if no referentiels exist
            createReferentiel("Web Development", "WEBDEV", "Covers all modern web technologies", "web-dev-cover.jpg");
            createReferentiel("Data Science", "DATASCI", "Focuses on data manipulation and analysis", "data-sci-cover.jpg");
            // Add more as needed
        }
    }

    private void createReferentiel(String libelle, String code, String description, String photo) {
        ReferentielEntity referentiel = new ReferentielEntity();
        referentiel.setLibelle(libelle);
        referentiel.setCode(code);
        referentiel.setDescription(description);
        referentiel.setPhotoCouverture(photo);
        referentiel.setStatus(StatusReferenceEnum.Actif);
        referentielRepository.save(referentiel);
    }
}
