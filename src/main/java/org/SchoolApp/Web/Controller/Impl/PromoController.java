package org.SchoolApp.Web.Controller.Impl;

import org.SchoolApp.Datas.Entity.PromoEntity;
import org.SchoolApp.Datas.Enums.EtatEnum;
import org.SchoolApp.Web.Controller.Interfaces.CrudController;
import org.SchoolApp.Web.Dtos.Request.PromoRequest;
import org.SchoolApp.Services.Impl.PromoService;
import org.SchoolApp.Web.Dtos.Request.PromoUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Promotions")
public class PromoController  {

    @Autowired
    private PromoService promoService;

    @PostMapping("")
    public PromoEntity create(@RequestBody PromoRequest promoRequest) {
        return promoService.createPromo(promoRequest);
    }

    @GetMapping("")
    public List<PromoEntity> findAll() {
        return promoService.getAllPromos();
    }

    @GetMapping("/encours/{referentielId}")
    public PromoEntity getPromoEncours(@PathVariable Long referentielId) {
        return promoService.getActivePromo(referentielId);
    }

    @PatchMapping("/{id}/etat")
    public List<PromoEntity> updateEtatPromo(@PathVariable Long id, @RequestBody EtatEnum etat) {
        return promoService.updateEtat(id, etat);
    }

    @GetMapping("/{id}")
    public PromoEntity findById(@PathVariable Long id) {
        return promoService.getById(id);
    }

    @PatchMapping("/{id}")
    public PromoEntity update(@PathVariable Long id, @RequestBody PromoUpdateRequest promoUpdateRequest) {
        return promoService.update(id, promoUpdateRequest);
    }

    @PatchMapping("/{id}/cloturer")
    public PromoEntity cloturerPromo(@PathVariable Long id) {
        return promoService.cloturePromo(id);
    }
}
