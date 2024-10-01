package org.SchoolApp.Datas.Repository;

import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprenantRepository extends SoftDeleteRepository<ApprenantEntity, Long> {

    @Query("SELECT a FROM ApprenantEntity a " +
            "JOIN a.referentiel r " +
            "JOIN r.promos p " +
            "WHERE p.etat = 'ACTIF' AND p.deleted = false AND r.id = :referentielId")
    List<ApprenantEntity> findApprenantsByReferentielAndActivePromo(@Param("referentielId") Long referentielId);

    @Query("SELECT a FROM ApprenantEntity a " +
            "JOIN a.referentiel r " +
            "JOIN r.promos p " +
            "WHERE p.etat = 'ACTIF' AND p.deleted = false")
    List<ApprenantEntity> findApprenantsByActivePromo();

    @Query("SELECT a FROM ApprenantEntity a " +
            "JOIN a.referentiel r " +
            "JOIN r.promos p " +
            "WHERE p.id = :promoId AND p.deleted = false")
    List<ApprenantEntity> findApprenantsByPromoId(@Param("promoId") Long promoId);

    @Query("SELECT a FROM ApprenantEntity a " +
            "JOIN a.referentiel r " +
            "JOIN r.promos p " +
            "WHERE p.id = :promoId AND r.id = :referentielId AND p.deleted = false")
    List<ApprenantEntity> findApprenantsByPromoIdAndReferentielId(@Param("promoId") Long promoId, @Param("referentielId") Long referentielId);

    // Nouvelle méthode pour compter les apprenants dans un référentiel spécifique
    @Query("SELECT COUNT(a) FROM ApprenantEntity a WHERE a.referentiel.id = :referentielId")
    Long countApprenantsByReferentielId(@Param("referentielId") Long referentielId);

    boolean existsByUser_Email(String email);

}
