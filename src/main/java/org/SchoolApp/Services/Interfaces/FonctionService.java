package org.SchoolApp.Services.Interfaces;

import org.SchoolApp.Datas.Entity.FonctionEntity;

import java.util.List;

public interface FonctionService {
    List<FonctionEntity> getAllFonctions();
    FonctionEntity getFonctionById(Long id);
    FonctionEntity createFonction(FonctionEntity fonction);
    FonctionEntity updateFonction(Long id, FonctionEntity fonction);
    void deleteFonction(Long id);
}
