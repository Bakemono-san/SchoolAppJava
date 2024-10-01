package org.SchoolApp.Datas.Seeders;

import jakarta.annotation.PostConstruct;
import org.SchoolApp.Datas.Entity.CompetencesEntity;
import org.SchoolApp.Datas.Repository.CompetencesRepository;
import org.springframework.stereotype.Component;

@Component
public class CompetencesSeeder {
    private final CompetencesRepository competencesRepository;

    public CompetencesSeeder(CompetencesRepository competencesRepository) {
        this.competencesRepository = competencesRepository;
    }

    @PostConstruct
    public void seedCompetences() {
        if (competencesRepository.count() == 0) {
            createCompetence("Back-End", "Develop websites", 200, "Technical");
            createCompetence("Front-End", "Manage social media", 100, "Soft");
        }
    }

    private void createCompetence(String nom, String description, int duree, String type) {
        CompetencesEntity competence = new CompetencesEntity();
        competence.setNom(nom);
        competence.setDescription(description);
        competence.setDuree_acquisition(duree);
        competence.setType(type);
        competencesRepository.save(competence);
    }
}
