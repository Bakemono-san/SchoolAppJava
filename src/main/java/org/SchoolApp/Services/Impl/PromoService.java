package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.PromoEntity;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Enums.EtatEnum;
import org.SchoolApp.Datas.Repository.PromoRepository;
import org.SchoolApp.Datas.Repository.ReferentielRepository;
import org.SchoolApp.Datas.Request.PromoReferentiel;
import org.SchoolApp.Datas.Request.PromoRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PromoService {
    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    private ReferentielRepository referentielRepository;


    public PromoEntity createPromo(PromoRequest promoRequest) {
        PromoEntity promoEntity = new PromoEntity();
        promoEntity.setLibelle(promoRequest.getLibelle());
        promoEntity.setDate_debut(promoRequest.getDateDebut());
        promoEntity.setDate_fin(promoRequest.getDateFin());
        promoEntity.setDuree(promoRequest.getDuree());
        promoEntity.setEtat(promoRequest.getEtat());
        if(promoRequest.getReferentiels() != null){
            promoEntity.setReferentiels(promoRequest.getReferentiels().get());
        }
        return promoRepository.save(promoEntity);
    }

    public List<PromoEntity> getAllPromos(){
        return promoRepository.findAll();
    }

    public PromoEntity getPromoByLibelle(String libelle){
        return promoRepository.findByLibelle(libelle);
    }

    public Optional<PromoEntity> getById(Long id){
        return  promoRepository.findById(id);
    }

    public PromoEntity getActivePromo(){
        return promoRepository.findActivePromo();
    }

    public List<PromoEntity> updateEtat(Long id, EtatEnum etat){
        Optional<PromoEntity> promo = getById(id);

        if(promo.isPresent()){
            promo.get().setEtat(etat);
            List<PromoEntity> promos = getAllPromos();
            if(etat.equals(EtatEnum.ACTIF)){
                for(PromoEntity promoEntity : promos){
                    if(!Objects.equals(promoEntity.getId(), id)){
                        promoEntity.setEtat(EtatEnum.INACTIF);
                        promoRepository.save(promoEntity);
                    }
                }
            }
             promoRepository.save(promo.get());
            return promos;
        }else{
            throw new IllegalArgumentException("Le promo n'existe pas");
        }
    }

    public PromoEntity cloturePromo(Long id){
        Optional<PromoEntity> promo = getById(id);
        Date today = new Date();


        if(promo.isPresent() && promo.get().getDate_fin().before(today)){
            promo.get().setEtat(EtatEnum.CLOTURE);
            return promoRepository.save(promo.get());
        }else{
            throw new IllegalArgumentException("La Date de fin de promo n'est pas encore arrivee");
        }
    }

    public PromoEntity updatePromo(Long id,String libelle, Date dateDebut, Date dateFin, int duree, boolean deleted, EtatEnum etat ){
        Optional<PromoEntity> promo = getById(id);

        if(promo.isPresent()){
            if(!libelle.isEmpty()){
                promo.get().setLibelle(libelle);
            }

            if(dateDebut!=null){
                promo.get().setDate_debut(dateDebut);
            }

            if(dateFin!=null){
                promo.get().setDate_fin(dateFin);
            }

            if(duree!=0){
                promo.get().setDuree(duree);
            }

            if(etat!=null){
                promo.get().setEtat(etat);
            }

            return promoRepository.save(promo.get());
        }else {
            throw new IllegalArgumentException("Le promo n'existe pas");
        }
    }

    public PromoEntity addOrDeleteReferentiel(Long promoId,Long referentielId, boolean add){
        PromoEntity promo = getById(promoId).orElseThrow(() -> new IllegalArgumentException("Promotion not found"));

            ReferentielEntity referentiel = referentielRepository.findById(referentielId).orElseThrow(() -> new IllegalArgumentException("Referentiel not found"));
            if(add){
                promo.getReferentiels().add(referentiel);
            }else {
                promo.getReferentiels().remove(referentiel);
            }

        return promoRepository.save(promo);
    }

    @Transactional
    public Set<PromoReferentiel> findReferentielActif(Long promoId){
        PromoEntity promo = getById(promoId).orElseThrow(() -> new IllegalArgumentException("Promotion not found"));

        return promo.getReferentiels().stream().map(
                referentiel -> {
                    PromoReferentiel referentielEntity = new PromoReferentiel();
                    referentielEntity.setCode(referentiel.getCode());
                    referentielEntity.setLibelle(referentiel.getLibelle());
                    return referentielEntity;
                }
        ).collect(Collectors.toSet());
    }
}
