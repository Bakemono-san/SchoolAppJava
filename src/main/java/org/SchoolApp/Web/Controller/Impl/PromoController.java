package org.SchoolApp.Web.Controller.Impl;

import org.SchoolApp.Datas.Entity.PromoEntity;
import org.SchoolApp.Datas.Enums.EtatEnum;
import org.SchoolApp.Datas.Request.PromoRequest;
import org.SchoolApp.Services.Impl.PromoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Promotions")
public class PromoController {
    @Autowired
    private PromoService promoService;

    @PostMapping("")
    public PromoEntity createPromo(@RequestBody PromoRequest promoEntity){
        return promoService.createPromo(promoEntity);
    }

    @GetMapping("")
    public List<PromoEntity> getAllPromos(){
        return promoService.getAllPromos();
    }

    @GetMapping("/encours")
    public PromoEntity getPromoEncours(){
        return promoService.getActivePromo();
    }

    @PatchMapping("/{id}/etat")
    public List<PromoEntity> updateEtatPromo(@PathVariable Long id, @RequestBody EtatEnum etat){
        return promoService.updateEtat(id, etat);
    }

    @PatchMapping("/{id}/cloturer")
    public PromoEntity cloturerPromo(@PathVariable Long id){
        return promoService.cloturePromo(id);
    }
}
