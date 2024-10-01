package org.SchoolApp.Web.Dtos.Request;

import lombok.Data;
import java.util.List;

@Data
public class ReferentielRequestDto {
    private String libelle;
    private String code;
    private String description;
    private String photoCouverture;  // Field name matches entity for proper mapping
    private List<CompetenceRequestDto> competences;  // Competence relationship if necessary
}
