package org.SchoolApp.Datas.Repository;

import org.SchoolApp.Datas.Entity.PromoEntity;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface PromoRepository extends SoftDeleteRepository<PromoEntity,Long> {
    @Query("SELECT p FROM PromoEntity p WHERE p.etat = 'ACTIF'")
    PromoEntity findActivePromo();

    PromoEntity findByLibelle(String libelle);

    @Modifying
    @Transactional
    @Query(value = "UPDATE promo_referentiel "
            + "SET deleted = true "
            + "WHERE promo_id = :promoId AND referentiel_id = :referentielId", nativeQuery = true)
    void softDeleteAssociation(@Param("promoId") Long promoId, @Param("referentielId") Long referentielId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE promo_referentiel "
            + "SET deleted = false "
            + "WHERE promo_id = :promoId AND referentiel_id = :referentielId", nativeQuery = true)
    void restoreAssociation(@Param("promoId") Long promoId, @Param("referentielId") Long referentielId);



}
