package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.PromoEntity;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Enums.EtatEnum;
import org.SchoolApp.Datas.Enums.StatusReferenceEnum;
import org.SchoolApp.Datas.Repository.ApprenantRepository;
import org.SchoolApp.Datas.Repository.PromoRepository;
import org.SchoolApp.Datas.Repository.ReferentielRepository;
import org.SchoolApp.Web.Dtos.Request.PromoReferentiel;
import org.SchoolApp.Web.Dtos.Request.PromoRequest;
import org.SchoolApp.Web.Dtos.Request.PromoUpdateRequest;
import org.odc.core.Exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.SchoolApp.Web.Dtos.Request.ReferentielRequestDto;

import java.time.*;
import java.util.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PromoService {

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private ReferentielRepository referentielRepository;

    private ApprenantRepository apprenantRepository;

    @Transactional
    public PromoEntity createPromo(PromoRequest promoRequest) {
        PromoEntity promoEntity = new PromoEntity();
        promoEntity.setLibelle(promoRequest.getLibelle());

        // Calculer la durée ou la date de fin en fonction des champs fournis
        if (promoRequest.getDateDebut() != null && promoRequest.getDateFin() != null) {
            promoEntity.setDate_debut(promoRequest.getDateDebut());
            promoEntity.setDate_fin(promoRequest.getDateFin());

            long diffInMillis = promoRequest.getDateFin().getTime() - promoRequest.getDateDebut().getTime();
            int duree = (int) TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS) / 30;
            promoEntity.setDuree(duree);
        } else if (promoRequest.getDateDebut() != null && promoRequest.getDuree() > 0) {
            promoEntity.setDate_debut(promoRequest.getDateDebut());
            Calendar cal = Calendar.getInstance();
            cal.setTime(promoRequest.getDateDebut());
            cal.add(Calendar.MONTH, promoRequest.getDuree());
            promoEntity.setDate_fin(cal.getTime());
            promoEntity.setDuree(promoRequest.getDuree());
        } else {
            throw new IllegalArgumentException("Either dateDebut and dateFin or dateDebut and duree must be provided.");
        }

        // Définir l'état par défaut à INACTIF
        promoEntity.setEtat(EtatEnum.INACTIF);

        // Récupérer et associer les référentiels actifs
        if (promoRequest.getReferentiels() != null && !promoRequest.getReferentiels().isEmpty()) {
            List<String> referentielLibelles = promoRequest.getReferentiels().stream()
                    .map(ReferentielRequestDto::getLibelle)
                    .collect(Collectors.toList());

            Set<ReferentielEntity> referentiels = new HashSet<>(referentielRepository.findByLibelleIn(referentielLibelles).stream()
                    .filter(referentiel -> referentiel.getStatus() == StatusReferenceEnum.Actif)
                    .collect(Collectors.toList()));
            promoEntity.setReferentiels(referentiels);
        }

        return promoRepository.save(promoEntity);
    }




    @Transactional(readOnly = true)
    public List<PromoEntity> getAllPromos() {
        return promoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PromoEntity getPromoByLibelle(String libelle) {
        return promoRepository.findByLibelle(libelle)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion avec libellé '" + libelle + "' non trouvée."));
    }

    @Transactional(readOnly = true)
    public PromoEntity getById(Long id) {
        return promoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion avec ID " + id + " non trouvée."));
    }

    @Transactional(readOnly = true)
    public PromoEntity getActivePromo(Long referentielId) {
        List<PromoEntity> activePromos = promoRepository.findActivePromosByReferentielId(referentielId);
        if (activePromos.isEmpty()) {
            throw new ResourceNotFoundException("Aucune promotion active trouvée pour le référentiel ID " + referentielId + ".");
        }
        return activePromos.get(0); // Supposant qu'il n'y a qu'une seule promotion active par référentiel.
    }

    @Transactional
    public List<PromoEntity> updateEtat(Long id, EtatEnum etat) {
        PromoEntity promo = getById(id);
        promo.setEtat(etat);
        promoRepository.save(promo);

        if (etat == EtatEnum.ACTIF) {
            // Désactiver toutes les autres promotions actives
            List<PromoEntity> otherPromos = promoRepository.findAll().stream()
                    .filter(p -> !p.getId().equals(id) && p.getEtat() != EtatEnum.INACTIF)
                    .collect(Collectors.toList());
            otherPromos.forEach(otherPromo -> {
                otherPromo.setEtat(EtatEnum.INACTIF);
                promoRepository.save(otherPromo);
            });
        }
        return promoRepository.findAll();
    }


    @Transactional
    public PromoEntity cloturePromo(Long id) {
        PromoEntity promo = getById(id);
        LocalDate today = LocalDate.now();
        LocalDate endDate = promo.getDate_fin().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (endDate.isBefore(today)) {
            promo.setEtat(EtatEnum.CLOTURE);
            return promoRepository.save(promo);
        } else {
            throw new IllegalStateException("La date de fin de la promotion n'est pas encore atteinte.");
        }
    }



    @Transactional
    public void delete(Long id) {
        if (!promoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Promotion avec ID " + id + " non trouvée.");
        }
        promoRepository.softDelete(id);
    }

    private void ensurePromotionIsModifiable(PromoEntity promo) {
        if (promo.getEtat() == EtatEnum.CLOTURE) {
            throw new IllegalStateException("Cette promotion est clôturée et ne peut plus être modifiée.");
        }
    }

    @Transactional
    public PromoEntity update(Long id, PromoUpdateRequest request) {
        PromoEntity promo = getById(id);
        ensurePromotionIsModifiable(promo); 

        Optional.ofNullable(request.getLibelle()).ifPresent(promo::setLibelle);
        Optional.ofNullable(request.getDateDebut()).ifPresent(promo::setDate_debut);
        Optional.ofNullable(request.getDateFin()).ifPresent(promo::setDate_fin);
        if (request.getDuree() > 0) {
            promo.setDuree(request.getDuree());
        }
        Optional.ofNullable(request.getEtat()).ifPresent(promo::setEtat);

        return promoRepository.save(promo);
    }


    @Transactional
    public PromoEntity addOrDeleteReferentiel(Long promoId, Long referentielId, boolean add) {
        PromoEntity promo = getById(promoId);
        ensurePromotionIsModifiable(promo); // Vérification avant modification

        ReferentielEntity referentiel = referentielRepository.findById(referentielId)
                .orElseThrow(() -> new ResourceNotFoundException("Référentiel avec ID " + referentielId + " non trouvé."));

        if (add && !promo.getReferentiels().contains(referentiel)) {
            // Vérifier que le référentiel est actif avant d'ajouter
            if (referentiel.getStatus() != StatusReferenceEnum.Actif) {
                throw new IllegalArgumentException("Seuls les référentiels actifs peuvent être ajoutés.");
            }
            promo.getReferentiels().add(referentiel);
            promoRepository.restoreAssociation(promoId, referentielId);
        } else if (!add && promo.getReferentiels().contains(referentiel)) {
            // Vérifier si le référentiel est vide
            Long apprenantCount = apprenantRepository.countApprenantsByReferentielId(referentielId);
            if (apprenantCount == 0) {
                promo.getReferentiels().remove(referentiel);
                promoRepository.softDeleteAssociation(promoId, referentielId);
            } else {
                throw new IllegalStateException("Le référentiel contient des apprenants et ne peut pas être retiré.");
            }
        }

        return promoRepository.save(promo);
    }


    @Transactional(readOnly = true)
    public Set<PromoReferentiel> findReferentielActif(Long promoId) {
        PromoEntity promo = getById(promoId);
        return promo.getReferentiels().stream().map(referentiel -> {
            PromoReferentiel dto = new PromoReferentiel();
            dto.setCode(referentiel.getCode());
            dto.setLibelle(referentiel.getLibelle());
            return dto;
        }).collect(Collectors.toSet());
    }


}
