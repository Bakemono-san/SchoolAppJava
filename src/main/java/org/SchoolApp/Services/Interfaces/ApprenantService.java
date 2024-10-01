package org.SchoolApp.Services.Interfaces;

import org.SchoolApp.Datas.Entity.ApprenantEntity;

import java.util.List;

public interface ApprenantService {
    ApprenantEntity createApprenant(ApprenantEntity apprenant);

    List<ApprenantEntity> getAllApprenants();

    ApprenantEntity getApprenantById(Long id);

    ApprenantEntity updateApprenant(Long id, ApprenantEntity apprenantUpdates);

    void deleteApprenant(Long id);
}
