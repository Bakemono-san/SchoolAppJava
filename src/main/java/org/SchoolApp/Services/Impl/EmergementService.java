package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Datas.Entity.EmargementEntity;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Repository.ApprenantRepository;
import org.SchoolApp.Datas.Repository.EmargementRepository;
import org.SchoolApp.Datas.Repository.UserRepository;
import org.SchoolApp.Services.Interfaces.EmargementIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class EmergementService implements EmargementIService {

    @Autowired
    private EmargementRepository emargementRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApprenantRepository apprenantRepository;
    @Override
    public List<EmargementEntity> getEmargementsByApprenant(ApprenantEntity apprenant) {
        UserEntity user = apprenant.getUser();
        return emargementRepository.findByUser(user);
    }

    @Override
    public List<EmargementEntity> getEmargementsByApprenantAndMonth(Long apprenantId, int year, int month) {
        Optional<ApprenantEntity> apprenantOptional = apprenantRepository.findById(apprenantId);
        if (!apprenantOptional.isPresent()) {
            throw new IllegalArgumentException("L'apprenant avec l'ID " + apprenantId + " n'existe pas");
        }
        UserEntity user = apprenantOptional.get().getUser();

        // Calculate start and end of the month
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        return emargementRepository.findByUserAndDateBetween(user, startOfMonth, endOfMonth);
    }

    @Override
    public EmargementEntity getEmargementsByApprenantAndDate(Long apprenantId, LocalDate date) {
        Optional<ApprenantEntity> apprenantOptional = apprenantRepository.findById(apprenantId);
        if (apprenantOptional.isEmpty()) {
            throw new IllegalArgumentException("L'apprenant avec l'ID " + apprenantId + " n'existe pas");
        }
        UserEntity user = apprenantOptional.get().getUser();

        return emargementRepository.findByUserAndDate(user, date);
    }
    @Override
    public List<EmargementEntity> getEmargementsByApprenantId(Long apprenantId) {
        Optional<ApprenantEntity> apprenantOptional = apprenantRepository.findById(apprenantId);
        if (apprenantOptional.isEmpty()) {
            throw new IllegalArgumentException("L'apprenant avec l'ID " + apprenantId + " n'existe pas");
        }
        UserEntity user = apprenantOptional.get().getUser();

        return emargementRepository.findByUser(user);
    }

    @Override
    public EmargementEntity getEmargementByUserAndDate(UserEntity user, LocalDate date) {
        return emargementRepository.findByUserAndDate(user, date);
    }

    @Override
    public EmargementEntity getAbsencesByUserAndDate(UserEntity user, LocalDate date) {
        return emargementRepository.findByUserAndEntreeIsNullOrSortieIsNullAndDate(user, date);
    }

    @Override
    public List<EmargementEntity> getPresencesByUserBetweenDates(UserEntity user, LocalDate startDate, LocalDate endDate) {
        return emargementRepository.findByUserAndEntreeIsNotNullAndSortieIsNotNullAndDateBetween(user, startDate, endDate);
    }


    @Override
    public List<EmargementEntity> getEmargementsByMonth(UserEntity user, int year, int month) {
        // Calculate start and end of the month
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        // Return emargements filtered by date range
        return emargementRepository.findByUserAndDateBetween(user, startOfMonth, endOfMonth);
    }

    @Override
    public List<EmargementEntity> getAllEmargementsByUser(UserEntity user) {
        return emargementRepository.findByUser(user);
    }
    @Override
    public EmargementEntity checkInOrOut(UserEntity user) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        EmargementEntity existingEmargement = emargementRepository.findByUserAndDate(user, currentDate);

        if (existingEmargement != null) {
            // If already checked in, set check-out time
            if (existingEmargement.getSortie() == null) {
                existingEmargement.setSortie(currentTime);
                return emargementRepository.save(existingEmargement);
            } else {
                // Already checked in and out, no more emargement allowed
                throw new IllegalStateException("Already checked in and out for this date");
            }
        } else {
            // First check-in for the day
            EmargementEntity newEmargement = new EmargementEntity();
            newEmargement.setEntree(currentTime); // Set the current time for entry
            newEmargement.setUser(user); // Set the user association
            newEmargement.setDate(currentDate); // Set the date to today
            return emargementRepository.save(newEmargement);
        }
    }
    @Override
    public Map<String, Object> emargerUser(Long userId) {
        LocalDate currentDate = LocalDate.now();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> errors = new ArrayList<>();

        // Check if user exists
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            errors.add(Map.of(
                    "id", userId,
                    "error", "L'utilisateur avec l'ID " + userId + " n'existe pas"
            ));
            result.put("errors", errors);
            return result;
        }

        UserEntity user = userOptional.get();
        EmargementEntity existingEmargement = emargementRepository.findByUserAndDate(user, currentDate);

        // Check if the user has already emarged today
        if (existingEmargement != null && existingEmargement.getSortie() != null) {
            errors.add(Map.of(
                    "id", userId,
                    "error", "L'utilisateur avec l'ID " + userId + " a déjà émargé deux fois aujourd'hui"
            ));
            result.put("errors", errors);
            return result;
        }

        // Proceed to check in or out
        try {
            EmargementEntity emargement = checkInOrOut(user);
            result.put("successful", emargement);
        } catch (IllegalStateException e) {
            errors.add(Map.of(
                    "id", userId,
                    "error", e.getMessage()
            ));
        }

        result.put("errors", errors);
        return result;
    }
@Override
    public Map<String, Object> emargerUsers(List<Long> userIds) {
        Map<String, Object> finalResult = new HashMap<>();
        List<Map<String, Object>> errors = new ArrayList<>();
        List<EmargementEntity> successfulEmargements = new ArrayList<>();

        for (Long userId : userIds) {
            Map<String, Object> result = emargerUser(userId);
            if (result.containsKey("successful")) {
                successfulEmargements.add((EmargementEntity) result.get("successful"));
            }
            if (result.containsKey("errors")) {
                errors.addAll((List<Map<String, Object>>) result.get("errors"));
            }
        }

        finalResult.put("successful", successfulEmargements);
        finalResult.put("errors", errors);
        return finalResult;
    }
    @Override
    public Map<String, Object> emargerApprenant(Long apprenantId) {
        LocalDate currentDate = LocalDate.now();
        Map<String, Object> result = new HashMap<>();

        Optional<ApprenantEntity> apprenantOptional = apprenantRepository.findById(apprenantId);
        if (!apprenantOptional.isPresent()) {
            result.put("id", apprenantId);
            result.put("error", "L'apprenant avec l'ID " + apprenantId + " n'existe pas");
            return result;
        }

        ApprenantEntity apprenant = apprenantOptional.get();
        UserEntity user = apprenant.getUser();

        EmargementEntity existingEmargement = emargementRepository.findByUserAndDate(user, currentDate);
        if (existingEmargement != null && existingEmargement.getSortie() != null) {
            result.put("id", apprenantId);
            result.put("error", "L'utilisateur de l'apprenant avec l'ID " + apprenantId + " a déjà émargé deux fois aujourd'hui");
            return result;
        }

        try {
            EmargementEntity emargement = checkInOrOut(user);
            result.put("id", apprenantId);
            result.put("emargement", emargement);
        } catch (IllegalStateException e) {
            result.put("id", apprenantId);
            result.put("error", e.getMessage());
        }

        return result;
    }

    // Méthode pour émarger plusieurs apprenants
    @Override
    public Map<String, Object> emargerApprenants(List<Long> apprenantIds) {
        List<Map<String, Object>> errors = new ArrayList<>();
        List<EmargementEntity> successfulEmargements = new ArrayList<>();

        for (Long apprenantId : apprenantIds) {
            Map<String, Object> result = emargerApprenant(apprenantId);
            if (result.containsKey("error")) {
                errors.add(result);
            } else {
                successfulEmargements.add((EmargementEntity) result.get("emargement"));
            }
        }

        return Map.of(
                "successful", successfulEmargements,
                "errors", errors
        );
    }
    @Override
    public Map<String, Object> updateEmargement(Long apprenantId, LocalTime entree, LocalTime sortie) {
        Map<String, Object> result = new HashMap<>();
        LocalDate currentDate = LocalDate.now();

        Optional<ApprenantEntity> apprenantOptional = apprenantRepository.findById(apprenantId);
        if (!apprenantOptional.isPresent()) {
            result.put("error", "L'apprenant avec l'ID " + apprenantId + " n'existe pas");
            return result;
        }

        ApprenantEntity apprenant = apprenantOptional.get();
        UserEntity user = apprenant.getUser();

        EmargementEntity existingEmargement = emargementRepository.findByUserAndDate(user, currentDate);
        if (existingEmargement == null) {
            result.put("error", "Aucun émargement trouvé pour l'apprenant avec l'ID " + apprenantId + " pour aujourd'hui");
            return result;
        }

        if (entree != null) {
            existingEmargement.setEntree(entree);
        }
        if (sortie != null) {
            existingEmargement.setSortie(sortie);
        }

        emargementRepository.save(existingEmargement);

        result.put("emargement", existingEmargement);
        return result;
    }
    @Override
    public void markAbsencesForToday() {
        LocalDate currentDate = LocalDate.now();

        List<ApprenantEntity> apprenants = apprenantRepository.findAll();

        for (ApprenantEntity apprenant : apprenants) {
            UserEntity user = apprenant.getUser();
            EmargementEntity emargement = emargementRepository.findByUserAndDate(user, currentDate);

            if (emargement == null || (emargement.getEntree() != null && emargement.getSortie() == null) || (emargement.getEntree() == null && emargement.getSortie() != null)) {
                // Marquer comme absent
                // Ici, vous pouvez mettre à jour l'entité apprenant ou enregistrer un nouveau record d'absence
                apprenant.setDeleted(true); // Ex.: Marquer comme supprimé, ou créez un objet d'absence

                // Vous pouvez aussi mettre à jour l'émargement pour que l'entrée ou la sortie soit nulle
                if (emargement != null) {
                    if (emargement.getSortie() == null) {
                        emargement.setSortie(null); // Mettre la sortie à null
                    } else if (emargement.getEntree() == null) {
                        emargement.setEntree(null); // Mettre l'entrée à null
                    }
                    emargementRepository.save(emargement); // Sauvegarder les modifications
                }
            }
        }

    }

    @Override
    public List<EmargementEntity> emargementAll(Integer mois, Integer annee, Long referentielId, LocalDate date, Long promoId) {
        List<ApprenantEntity> apprenants;

        if (referentielId != null && promoId == null) {
            apprenants = apprenantRepository.findApprenantsByReferentielAndActivePromo(referentielId);
        } else if (referentielId != null && promoId != null) {
            apprenants = apprenantRepository.findApprenantsByPromoIdAndReferentielId(promoId,referentielId);
        } else if (promoId != null) {
            apprenants = apprenantRepository.findApprenantsByPromoId(promoId);
        } else {
            apprenants = apprenantRepository.findApprenantsByActivePromo();
        }
        List<EmargementEntity> emargements = new ArrayList<>();
        for (ApprenantEntity apprenant : apprenants) {
            UserEntity user = apprenant.getUser();

            if (mois != null) {
                int year = (annee != null) ? annee : LocalDate.now().getYear();
                emargements.addAll(getEmargementsByMonth(user, year, mois));
            } else if (date != null) {
                EmargementEntity emargement = getEmargementByUserAndDate(user, date);
                if (emargement != null) {
                    emargements.add(emargement);
                }
            } else {
                emargements.addAll(getAllEmargementsByUser(user));
            }
        }
        return emargements;
    }







}
