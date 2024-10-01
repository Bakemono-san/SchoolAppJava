package org.SchoolApp.Datas.Repository;

import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Enums.StatusReferenceEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferentielRepository extends SoftDeleteRepository<ReferentielEntity, Long> {
    List<ReferentielEntity> findByStatus(StatusReferenceEnum status);

    List<ReferentielEntity> findByDeletedTrue();

    boolean existsByLibelle(String libelle);
    boolean existsByCode(String code);

    List<ReferentielEntity> findByLibelleIn(List<String> libelles);

}
