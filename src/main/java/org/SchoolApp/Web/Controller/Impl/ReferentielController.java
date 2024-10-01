package org.SchoolApp.Web.Controller.Impl;

import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Enums.StatusReferenceEnum;
import org.SchoolApp.Services.Interfaces.ReferentielService;
import org.SchoolApp.Web.Dtos.Mapper.ReferentielMapper;
import org.SchoolApp.Web.Dtos.Request.ReferentielRequestDto;
import org.SchoolApp.Web.Dtos.Request.ReferentielUpdateRequestDto;
import org.SchoolApp.Web.Dtos.Response.ReferentielResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/referentiels")
public class ReferentielController {

    @Autowired
    private ReferentielService referentielService;

    @Autowired
    private ReferentielMapper referentielMapper;

    @PostMapping
    public ResponseEntity<?> createReferentiel(@Validated @RequestBody ReferentielRequestDto referentielRequestDto) {
        try {
            var referentielEntity = referentielMapper.toEntity(referentielRequestDto);
            var savedReferentiel = referentielService.createReferentiel(referentielEntity);
            var responseDto = referentielMapper.toDto(savedReferentiel);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la création du référentiel.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> listReferentiels(
            @RequestParam(required = false) StatusReferenceEnum status) {
        try {
            List<ReferentielEntity> referentiels;
            if (status != null) {
                referentiels = referentielService.listReferentielsByStatus(status);
            } else {
                referentiels = referentielService.listActiveReferentiels();
            }
            List<ReferentielResponseDto> responseDtos = referentiels.stream()
                    .map(referentielMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la récupération des référentiels.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReferentiel(@PathVariable Long id) {
        try {
            referentielService.deleteReferentiel(id);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la suppression du référentiel.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/archive")
    public ResponseEntity<?> listArchivedReferentiels() {
        try {
            List<ReferentielEntity> archivedReferentiels = referentielService.listArchivedReferentiels();
            List<ReferentielResponseDto> responseDtos = archivedReferentiels.stream()
                    .map(referentielMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la récupération des référentiels archivés.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReferentielById(
            @PathVariable Long id,
            @RequestParam(required = false) Long competenceId,
            @RequestParam(required = false) Long moduleId) {
        try {
            ReferentielEntity referentiel = referentielService.getReferentielById(id);
            if (referentiel == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Referentiel non trouvé.");
            }

            ReferentielResponseDto referentielDto = referentielMapper.toDto(referentiel);
            if (competenceId != null) {
                referentielDto.setCompetences(referentielDto.getCompetences().stream()
                        .filter(competence -> competence.getId().equals(competenceId))
                        .collect(Collectors.toList()));
            }

            if (moduleId != null) {
                referentielDto.getCompetences().forEach(competence -> {
                    competence.setModules(competence.getModules().stream()
                            .filter(module -> module.getId().equals(moduleId))
                            .collect(Collectors.toList()));
                });
            }

            return ResponseEntity.ok(referentielDto);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la récupération du référentiel.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> modifyReferentiel(
            @PathVariable Long id,
            @Validated @RequestBody ReferentielUpdateRequestDto referentielUpdateRequestDto) {
        try {
            ReferentielEntity updates = referentielMapper.toEntity(referentielUpdateRequestDto);
            ReferentielEntity updatedReferentiel = referentielService.updateReferentielWithDetails(id, updates);
            ReferentielResponseDto responseDto = referentielMapper.toDto(updatedReferentiel);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la mise à jour du référentiel.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
