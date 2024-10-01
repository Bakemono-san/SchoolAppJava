package org.SchoolApp.Datas.Seeders;

import jakarta.annotation.PostConstruct;
import org.SchoolApp.Datas.Entity.PromoEntity;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Enums.EtatEnum;
import org.SchoolApp.Datas.Repository.PromoRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class PromoSeeder {

    private final PromoRepository promoRepository;

    public PromoSeeder(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    @PostConstruct
    public void seedPromos() {
        if (promoRepository.count() == 0) {
            Set<ReferentielEntity> referentiels = new HashSet<>();
            createPromo("2025 Development Batch", new Date(), new Date(System.currentTimeMillis() + 31536000000L), 365, EtatEnum.ACTIF, referentiels);
        }
    }

    private void createPromo(String libelle, Date dateDebut, Date dateFin, int duree, EtatEnum etat, Set<ReferentielEntity> referentiels) {
        PromoEntity promo = new PromoEntity();
        promo.setLibelle(libelle);
        promo.setDate_debut(dateDebut);
        promo.setDate_fin(dateFin);
        promo.setDuree(duree);
        promo.setEtat(etat);
        promo.setReferentiels(referentiels);
        promoRepository.save(promo);
    }
}
