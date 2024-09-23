package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.CompetencesEntity;
import org.SchoolApp.Datas.Entity.ModulesEntity;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Enums.StatusReferenceEnum;
import org.SchoolApp.Services.Interfaces.ReferentielService;
import org.SchoolApp.Datas.Repository.ReferentielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.SchoolApp.Datas.Enums.EtatEnum;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReferentielServiceImpl implements ReferentielService {

    @Autowired
    private ReferentielRepository referentielRepository;

    @Override
    public List<ReferentielEntity> getAllReferentiels() {
        return referentielRepository.findAll();
    }

    @Override
    public List<ReferentielEntity> getActiveReferentiels() {
        return referentielRepository.findByStatus(StatusReferenceEnum.Actif);
    }

    @Override
    public List<ReferentielEntity> getDeletedReferentiels() {
        return referentielRepository.findByDeletedTrue();
    }

    @Override
    public List<ReferentielEntity> getReferentielsByStatus(StatusReferenceEnum status) {
        return referentielRepository.findByStatus(status);
    }

    @Override
    public ReferentielEntity getReferentielById(Long id) {
        return referentielRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Referentiel not found"));
    }


    @Override
    public ReferentielEntity createReferentiel(ReferentielEntity referentiel, List<CompetencesEntity> competences) {
        referentiel.setCompetences(new HashSet<>(competences));
        return referentielRepository.save(referentiel);
    }

    @Override
    public ReferentielEntity updateReferentiel(Long id, ReferentielEntity referentiel) {
        ReferentielEntity existingReferentiel = referentielRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Referentiel not found"));
        existingReferentiel.setLibelle(referentiel.getLibelle());
        existingReferentiel.setCode(referentiel.getCode());
        existingReferentiel.setDescription(referentiel.getDescription());
        existingReferentiel.setPhoto_couverture(referentiel.getPhoto_couverture());
        existingReferentiel.setStatus(referentiel.getStatus());
        return referentielRepository.save(existingReferentiel);
    }

    @Override
    public void deleteReferentiel(Long id) {
        ReferentielEntity referentiel = getReferentielById(id);
        boolean isInActivePromo = referentiel.getPromos().stream()
                .anyMatch(promo -> promo.getEtat() == EtatEnum.ACTIF && !promo.isDeleted());

        if (isInActivePromo) {
            throw new RuntimeException("Cannot delete a referentiel used in an active promotion");
        }
        referentiel.setDeleted(true);
        referentiel.setDeletedAt(LocalDateTime.now());
        referentielRepository.save(referentiel);
    }

    @Override
    public List<CompetencesEntity> getCompetencesWithModulesByReferentiel(Long referentielId) {
        ReferentielEntity referentiel = getReferentielById(referentielId);
        return referentiel.getCompetences().stream().filter(c -> !c.isDeleted()).collect(Collectors.toList());
    }

    @Override
    public List<ModulesEntity> getModulesByReferentiel(Long referentielId) {
        ReferentielEntity referentiel = getReferentielById(referentielId);
        return referentiel.getCompetences().stream()
                .flatMap(competence -> competence.getModules().stream())
                .filter(module -> !module.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public CompetencesEntity addCompetenceToReferentiel(Long referentielId, CompetencesEntity competence) {
        ReferentielEntity referentiel = getReferentielById(referentielId);
        referentiel.getCompetences().add(competence);
        return competence;
    }

    @Override
    public ModulesEntity addModuleToCompetence(Long referentielId, Long competenceId, ModulesEntity module) {
        ReferentielEntity referentiel = getReferentielById(referentielId);
        CompetencesEntity competence = referentiel.getCompetences().stream()
                .filter(c -> c.getId().equals(competenceId) && !c.isDeleted())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Competence not found"));
        competence.getModules().add(module);
        return module;
    }

    @Override
    public void deleteCompetenceFromReferentiel(Long referentielId, Long competenceId) {
        ReferentielEntity referentiel = getReferentielById(referentielId);
        CompetencesEntity competence = referentiel.getCompetences().stream()
                .filter(c -> c.getId().equals(competenceId) && !c.isDeleted())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Competence not found"));
        competence.setDeleted(true);
        competence.getModules().forEach(module -> module.setDeleted(true)); // Soft delete des modules
    }

    @Override
    public void deleteModuleFromCompetence(Long referentielId, Long competenceId, Long moduleId) {
        ReferentielEntity referentiel = getReferentielById(referentielId);
        CompetencesEntity competence = referentiel.getCompetences().stream()
                .filter(c -> c.getId().equals(competenceId) && !c.isDeleted())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Competence not found"));
        ModulesEntity module = competence.getModules().stream()
                .filter(m -> m.getId().equals(moduleId) && !m.isDeleted())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Module not found"));
        module.setDeleted(true);
    }
}
