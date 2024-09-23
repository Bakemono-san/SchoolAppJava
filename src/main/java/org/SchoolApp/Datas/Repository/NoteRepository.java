package org.SchoolApp.Datas.Repository;

import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Datas.Entity.NotesEntity;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Enums.EtatEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface NoteRepository extends SoftDeleteRepository<NotesEntity, Long> {
    List<NotesEntity> findByApprenant(ApprenantEntity apprenant);
    List<NotesEntity> findByApprenantAndModule(ApprenantEntity apprenant, Module module);
    @Query("SELECT n FROM NotesEntity n " +
            "WHERE n.apprenant.referentiel.id = :referentielId " +
            "AND EXISTS (SELECT p FROM n.apprenant.referentiel.promos p WHERE p.etat = :etat)")
    List<NotesEntity> findByApprenant_Referentiel_IdAndActivePromo(
            @Param("referentielId") Long referentielId,
            @Param("etat") EtatEnum etat);
}
