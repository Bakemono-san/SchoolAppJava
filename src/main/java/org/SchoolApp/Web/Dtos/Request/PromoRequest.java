package org.SchoolApp.Web.Dtos.Request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.SchoolApp.Datas.Enums.EtatEnum;

import java.util.Date;
import java.util.List;


@Data
public class PromoRequest {
    @NotBlank
    private String libelle;

    @NotBlank
    private Date dateDebut;
    @NotBlank
    @Future
    private Date dateFin;
    @NotBlank
    private boolean deleted;
    @NotBlank
    private int duree;
    @NotBlank
    private EtatEnum etat;
    private List<ReferentielRequestDto> referentiels;
}
