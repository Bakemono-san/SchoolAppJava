package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.CompetencesEntity;
import org.SchoolApp.Datas.Entity.ModulesEntity;
import org.SchoolApp.Datas.Entity.PromoEntity;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Enums.StatusReferenceEnum;
import org.SchoolApp.Datas.Repository.ModulesRepository;
import org.SchoolApp.Datas.Repository.PromoRepository;
import org.SchoolApp.Datas.Repository.ReferentielRepository;
import org.SchoolApp.Datas.Repository.CompetencesRepository;
import org.SchoolApp.Services.Interfaces.ReferentielService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReferentielServiceImpl implements ReferentielService {

    @Autowired
    private ReferentielRepository referentielRepository;

    @Autowired
    private CompetencesRepository competencesRepository;

    @Autowired
    private ModulesRepository modulesRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Override
    public ReferentielEntity createReferentiel(ReferentielEntity referentiel) {
        if (referentielRepository.existsByLibelle(referentiel.getLibelle())) {
            throw new IllegalArgumentException("Le libellé est déjà utilisé.");
        }
        if (referentielRepository.existsByCode(referentiel.getCode())) {
            throw new IllegalArgumentException("Le code est déjà utilisé.");
        }

        if (referentiel.getCompetences() == null) {
            referentiel.setCompetences(new HashSet<>());
        }
        handleCompetenciesAndModules(referentiel);
        return referentielRepository.save(referentiel);
    }

    @Override
    public List<ReferentielEntity> listActiveReferentiels() {
        return referentielRepository.findByStatus(StatusReferenceEnum.Actif);
    }

    @Override
    public List<ReferentielEntity> listReferentielsByStatus(StatusReferenceEnum status) {
        return referentielRepository.findByStatus(status);
    }

    @Override
    public ReferentielEntity updateReferentiel(Long id, ReferentielEntity updates) {
        ReferentielEntity referentiel = referentielRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Referentiel not found"));

        // Vérifier l'unicité si libelle ou code sont mis à jour
        if (updates.getLibelle() != null && !updates.getLibelle().equals(referentiel.getLibelle())) {
            if (referentielRepository.existsByLibelle(updates.getLibelle())) {
                throw new IllegalArgumentException("Le libellé est déjà utilisé.");
            }
            referentiel.setLibelle(updates.getLibelle());
        }

        if (updates.getCode() != null && !updates.getCode().equals(referentiel.getCode())) {
            if (referentielRepository.existsByCode(updates.getCode())) {
                throw new IllegalArgumentException("Le code est déjà utilisé.");
            }
            referentiel.setCode(updates.getCode());
        }

        if (updates.getDescription() != null) {
            referentiel.setDescription(updates.getDescription());
        }

        if (updates.getPhotoCouverture() != null) {
            referentiel.setPhotoCouverture(updates.getPhotoCouverture());
        }

        if (updates.getStatus() != null) {
            referentiel.setStatus(updates.getStatus());
        }

        handleCompetenciesAndModules(updates);
        return referentielRepository.save(referentiel);
    }

    @Override
    public void deleteReferentiel(Long id) {
        ReferentielEntity referentiel = referentielRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Referentiel not found"));

        // Vérifier si le référentiel est utilisé dans des promotions actives
        List<PromoEntity> activePromos = promoRepository.findActivePromosByReferentielId(id);
        if (!activePromos.isEmpty()) {
            throw new IllegalStateException("Le référentiel est utilisé dans des promotions actives et ne peut pas être archivé.");
        }

        referentielRepository.softDelete(id);
    }

    @Override
    public List<ReferentielEntity> listArchivedReferentiels() {
        return referentielRepository.findByDeletedTrue();
    }

    @Override
    public ReferentielEntity getReferentielById(Long id) {
        return referentielRepository.findById(id).orElse(null);
    }

    @Override
    public ReferentielEntity updateReferentielWithDetails(Long id, ReferentielEntity updates) {
        ReferentielEntity existing = referentielRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Referentiel not found"));

        // Mise à jour des champs principaux
        updateFields(existing, updates);

        // Gestion des compétences et modules
        handleCompetenciesAndModules(existing, updates);

        return referentielRepository.save(existing);
    }

    private void updateFields(ReferentielEntity referentiel, ReferentielEntity updates) {
        if (updates.getLibelle() != null) referentiel.setLibelle(updates.getLibelle());
        if (updates.getCode() != null) referentiel.setCode(updates.getCode());
        if (updates.getDescription() != null) referentiel.setDescription(updates.getDescription());
        if (updates.getPhotoCouverture() != null) referentiel.setPhotoCouverture(updates.getPhotoCouverture());
        if (updates.getStatus() != null) referentiel.setStatus(updates.getStatus());
    }

    /**
     * Gère l'ajout des compétences et modules lors de la création ou mise à jour d'un référentiel.
     *
     * @param referentiel le référentiel à traiter
     */
    private void handleCompetenciesAndModules(ReferentielEntity referentiel) {
        referentiel.getCompetences().forEach(competence -> {
            if (competence.getId() == null) {
                // Sauvegarder les modules qui n'ont pas encore d'ID
                if (competence.getModules() != null) {
                    competence.getModules().forEach(module -> {
                        if (module.getId() == null) {
                            modulesRepository.save(module);
                        }
                    });
                }
                competencesRepository.save(competence);
            }
        });
    }

    private void handleCompetenciesAndModules(ReferentielEntity existing, ReferentielEntity updates) {
        // Map des compétences existantes pour un accès rapide
        Map<Long, CompetencesEntity> existingCompetencesMap = existing.getCompetences().stream()
                .collect(Collectors.toMap(CompetencesEntity::getId, Function.identity()));

        // Parcourir les compétences mises à jour
        for (CompetencesEntity updateComp : updates.getCompetences()) {
            if (updateComp.getId() == null) {
                // Nouvelle compétence
                if (updateComp.getModules() != null) {
                    updateComp.getModules().forEach(module -> {
                        if (module.getId() == null) {
                            modulesRepository.save(module);
                        }
                    });
                }
                competencesRepository.save(updateComp);
                existing.getCompetences().add(updateComp);
            } else {
                // Compétence existante, mise à jour
                CompetencesEntity existingComp = existingCompetencesMap.get(updateComp.getId());
                if (existingComp != null) {
                    existingComp.setNom(updateComp.getNom());
                    existingComp.setDescription(updateComp.getDescription());
                    existingComp.setDuree_acquisition(updateComp.getDuree_acquisition());
                    existingComp.setType(updateComp.getType());

                    // Map des modules existants pour un accès rapide
                    Map<Long, ModulesEntity> existingModulesMap = existingComp.getModules().stream()
                            .collect(Collectors.toMap(ModulesEntity::getId, Function.identity()));

                    // Parcourir les modules mis à jour
                    for (ModulesEntity updateModule : updateComp.getModules()) {
                        if (updateModule.getId() == null) {
                            // Nouveau module
                            modulesRepository.save(updateModule);
                            existingComp.getModules().add(updateModule);
                        } else {
                            // Module existant, mise à jour
                            ModulesEntity existingModule = existingModulesMap.get(updateModule.getId());
                            if (existingModule != null) {
                                existingModule.setNom(updateModule.getNom());
                                existingModule.setDescription(updateModule.getDescription());
                                existingModule.setDuree_acquisition(updateModule.getDuree_acquisition());
                                modulesRepository.save(existingModule);
                            }
                        }
                    }

                    // Supprimer les modules qui ne sont plus présents dans les mises à jour
                    existingComp.getModules().removeIf(existingModule ->
                            !updateComp.getModules().stream()
                                    .anyMatch(updateModule -> updateModule.getId() != null && updateModule.getId().equals(existingModule.getId()))
                    );

                } else {
                    throw new RuntimeException("Compétence avec ID " + updateComp.getId() + " non trouvée dans le référentiel.");
                }
            }
        }

        // Supprimer les compétences qui ne sont plus présentes dans les mises à jour
        existing.getCompetences().removeIf(existingComp ->
                updates.getCompetences().stream()
                        .noneMatch(updateComp -> updateComp.getId() != null && updateComp.getId().equals(existingComp.getId()))
        );
    }
}
