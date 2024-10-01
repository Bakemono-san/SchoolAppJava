package org.SchoolApp.Web.Controller.Impl;

import org.SchoolApp.Services.Impl.ApprenantImportService;
import org.SchoolApp.Services.Interfaces.ApprenantService;
import org.SchoolApp.Web.Dtos.Mapper.ApprenantMapper;
import org.SchoolApp.Web.Dtos.Request.ApprenantRequestDto;
import org.SchoolApp.Web.Dtos.Response.ApprenantResponseDto;
import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/apprenants")
public class ApprenantController {

    private final ApprenantService apprenantService;
    private final ApprenantMapper apprenantMapper;
    private final ApprenantImportService apprenantImportService;

    @Autowired
    public ApprenantController(ApprenantService apprenantService, ApprenantMapper apprenantMapper, ApprenantImportService apprenantImportService) {
        this.apprenantService = apprenantService;
        this.apprenantMapper = apprenantMapper;
        this.apprenantImportService = apprenantImportService;
    }

    @PostMapping
    public ResponseEntity<ApprenantResponseDto> createApprenant(@RequestBody ApprenantRequestDto apprenantRequestDto) {
        ApprenantEntity apprenantEntity = apprenantMapper.toEntity(apprenantRequestDto);
        ApprenantEntity savedApprenant = apprenantService.createApprenant(apprenantEntity);
        ApprenantResponseDto responseDto = apprenantMapper.toDto(savedApprenant);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ApprenantResponseDto>> getAllApprenants() {
        List<ApprenantEntity> apprenants = apprenantService.getAllApprenants();
        List<ApprenantResponseDto> responseDtos = Collections.singletonList(apprenantMapper.toDto((ApprenantEntity) apprenants));
        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprenantResponseDto> getApprenantById(@PathVariable Long id) {
        ApprenantEntity apprenant = apprenantService.getApprenantById(id);
        ApprenantResponseDto responseDto = apprenantMapper.toDto(apprenant);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApprenantResponseDto> updateApprenant(@PathVariable Long id, @RequestBody ApprenantRequestDto apprenantRequestDto) {
        ApprenantEntity updatedApprenant = apprenantService.updateApprenant(id, apprenantMapper.toEntity(apprenantRequestDto));
        ApprenantResponseDto responseDto = apprenantMapper.toDto(updatedApprenant);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApprenant(@PathVariable Long id) {
        apprenantService.deleteApprenant(id);
        return ResponseEntity.ok("Apprenant supprimé avec succès.");
    }

    @PostMapping("/import")
    public ResponseEntity<?> importApprenants(@RequestParam("file") MultipartFile file, @RequestParam Long referentielId) {
        try {
            List<ApprenantEntity> failedApprenants = apprenantImportService.importApprenants(file, referentielId);
            if (failedApprenants.isEmpty()) {
                return ResponseEntity.ok("Importation réussie.");
            } else {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(failedApprenants);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'importation.");
        }
    }
}
