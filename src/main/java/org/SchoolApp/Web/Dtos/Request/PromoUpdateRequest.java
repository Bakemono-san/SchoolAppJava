package org.SchoolApp.Web.Dtos.Request;

import lombok.Data;
import org.SchoolApp.Datas.Enums.EtatEnum;

import java.util.Date;
import java.util.List;

@Data
public class PromoUpdateRequest {
    private String libelle;
    private Date dateDebut;
    private Date dateFin;
    private boolean deleted;
    private int duree;
    private EtatEnum etat;
    private List<ReferentielRequestDto> referentiels;
}
