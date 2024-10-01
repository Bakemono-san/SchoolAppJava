package org.SchoolApp.Web.Controller.Impl;

import org.SchoolApp.Datas.Entity.EmargementEntity;
import org.SchoolApp.Services.Interfaces.EmargementIService;
import org.SchoolApp.Web.Dtos.Mapper.EmargementMapper;
import org.SchoolApp.Web.Dtos.Request.EmargementRequestDto;
import org.SchoolApp.Web.Dtos.Request.EmargementUpdateRequestDto;
import org.SchoolApp.Web.Dtos.Response.EmargementResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/emargements")
@Validated
public class EmargementController {

    @Autowired
    private EmargementIService emargementService;

    @Autowired
    private EmargementMapper emargementMapper;

    @PostMapping
    public ResponseEntity<?> enregistrerEmargementGroupe(@Valid @RequestBody EmargementRequestDto requestDto) {
        try {
            Map<String, Object> result = emargementService.emargerUsers(Collections.singletonList(requestDto.getApprenantId()));

            List<EmargementEntity> successful = (List<EmargementEntity>) result.get("successful");
            List<Map<String, Object>> errors = (List<Map<String, Object>>) result.get("errors");

            List<EmargementResponseDto> successfulDtos = successful.stream()
                    .map(emargementMapper::toDto)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("successful", successfulDtos);
            response.put("errors", errors);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            // Gérer les exceptions globalement avec GlobalExceptionHandler
            throw e;
        }
    }

    @PostMapping("/apprenants/{id}")
    public ResponseEntity<EmargementEntity> emargerApprenant(
            @PathVariable("id") Long id,
            @Valid @RequestBody EmargementRequestDto emargementDto) {

        if (!id.equals(emargementDto.getApprenantId())) {
            return ResponseEntity.badRequest().build();
        }

        EmargementEntity emargement = (EmargementEntity) emargementService.emargerUser(id);
        return ResponseEntity.ok(emargement);
    }

    @GetMapping
    public ResponseEntity<List<EmargementResponseDto>> listerEmargements(
            @RequestParam(required = false) Integer mois,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) Long referentielId,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long promoId) {

        List<EmargementEntity> emargements = emargementService.emargementAll(mois, annee, referentielId, date, promoId);
        List<EmargementResponseDto> responseDtos = emargements.stream()
                .map(emargementMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    @PatchMapping("/apprenants/{id}")
    public ResponseEntity<?> modifierEmargementApprenant(
            @PathVariable Long id,
            @Valid @RequestBody EmargementUpdateRequestDto requestDto) {

        try {
            Map<String, Object> result = emargementService.updateEmargement(id, requestDto);

            if (result.containsKey("emargement")) {
                EmargementEntity emargement = (EmargementEntity) result.get("emargement");
                EmargementResponseDto dto = emargementMapper.toDto(emargement);
                return ResponseEntity.ok(dto);
            } else {
                String error = (String) result.get("error");
                Map<String, String> errorResponse = Map.of("error", error);
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // Gérer les exceptions globalement avec GlobalExceptionHandler
            throw e;
        }
    }
}
