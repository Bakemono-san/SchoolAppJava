package org.SchoolApp.Datas.Repository;

import org.SchoolApp.Datas.Entity.CompetencesEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetencesRepository extends SoftDeleteRepository<CompetencesEntity, Long> {
}
