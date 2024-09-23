package org.SchoolApp.Web.Controller.Impl;

import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Datas.Entity.EmargementEntity;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Services.Interfaces.EmargementIService;
import org.SchoolApp.Web.Controller.Interfaces.EmargementIController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/emargements")
public class EmargementController implements EmargementIController {

    @Autowired
    private EmargementIService emargementService;

    @PostMapping("/emarger/apprenants")
    public Map<String, Object> emargerApprenants(@RequestBody List<Long> apprenantIds) {
        return emargementService.emargerApprenants(apprenantIds);
    }

    @PostMapping("/emarger/apprenant/{apprenantId}")
    public Map<String, Object> emargerApprenant(@PathVariable Long apprenantId) {
        return emargementService.emargerApprenant(apprenantId);
    }

    @GetMapping("/apprenant/{apprenantId}/emargements")
    public List<EmargementEntity> getEmargementsByApprenantId(@PathVariable Long apprenantId) {
        return emargementService.getEmargementsByApprenantId(apprenantId);
    }

    @GetMapping("/apprenant/{apprenantId}/emargements/month/{year}/{month}")
    public List<EmargementEntity> getEmargementsByApprenantAndMonth(
            @PathVariable Long apprenantId,
            @PathVariable int year,
            @PathVariable int month) {
        return emargementService.getEmargementsByApprenantAndMonth(apprenantId, year, month);
    }

    @GetMapping("/apprenant/{apprenantId}/emargements/date/{date}")
    public EmargementEntity getEmargementsByApprenantAndDate(
            @PathVariable Long apprenantId,
            @PathVariable LocalDate date) {
        return emargementService.getEmargementsByApprenantAndDate(apprenantId, date);
    }

    @PutMapping("/apprenant/{apprenantId}/update")
    public Map<String, Object> updateEmargement(
            @PathVariable Long apprenantId,
            @RequestParam(required = false) LocalTime entree,
            @RequestParam(required = false) LocalTime sortie) {
        return emargementService.updateEmargement(apprenantId, entree, sortie);
    }
    @Override
    public Map<String, Object> emargerUsers(List<Long> userIds){

        return emargementService.emargerUsers(userIds);
    }
    public Map<String, Object> emargerUser(Long userId){
        return emargementService.emargerUser(userId);
    }
    @GetMapping("/all")
    public ResponseEntity<List<EmargementEntity>> getEmargements(
            @RequestParam(required = false) Integer mois,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) Long referentielId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long promoId) {
        List<EmargementEntity> emargements = emargementService.emargementAll(mois, annee, referentielId, date, promoId);
        return ResponseEntity.ok(emargements);
    }

}
