package org.SchoolApp.Datas.Repository;

import org.SchoolApp.Datas.Entity.FonctionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FonctionRepository extends SoftDeleteRepository<FonctionEntity, Long> {
    FonctionEntity findByLibelle(String libelle);
}
