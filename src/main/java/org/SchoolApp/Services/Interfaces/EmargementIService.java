package org.SchoolApp.Services.Interfaces;

import jakarta.validation.Valid;
import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Datas.Entity.EmargementEntity;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Web.Dtos.Request.EmargementUpdateRequestDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface EmargementIService {
    List<EmargementEntity> getEmargementsByApprenant(ApprenantEntity apprenant);
    List<EmargementEntity> getEmargementsByApprenantAndMonth(Long apprenantId, int year, int month);
    EmargementEntity getEmargementsByApprenantAndDate(Long apprenantId, LocalDate date);
    List<EmargementEntity> getEmargementsByApprenantId(Long apprenantId);
    EmargementEntity getEmargementByUserAndDate(UserEntity user, LocalDate date);
    EmargementEntity getAbsencesByUserAndDate(UserEntity user, LocalDate date);
    List<EmargementEntity> getPresencesByUserBetweenDates(UserEntity user, LocalDate startDate, LocalDate endDate);
    List<EmargementEntity> getEmargementsByMonth(UserEntity user, int year, int month);
    List<EmargementEntity> getAllEmargementsByUser(UserEntity user);
    EmargementEntity checkInOrOut(UserEntity user);
    Map<String, Object> emargerUser(Long userId);
    Map<String, Object> emargerUsers(List<Long> userIds);
    Map<String, Object> emargerApprenant(Long apprenantId);
    Map<String, Object> emargerApprenants(List<Long> apprenantIds);

    Map<String, Object> updateEmargement(Long apprenantId, LocalTime entree, LocalTime sortie);

    void markAbsencesForToday();
    List<EmargementEntity> emargementAll(Integer mois, Integer annee, Long referentielId, LocalDate date, Long promoId);

    Map<String, Object> updateEmargement(Long id, @Valid EmargementUpdateRequestDto requestDto);
}
