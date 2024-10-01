package org.SchoolApp.Web.Dtos.Response;

import lombok.Data;
import java.util.List;

@Data
public class ReferentielResponseDto {
    private Long id;
    private String libelle;
    private String code;
    private String description;
    private String photoCouverture;  // Field name matches entity
    private String status;  // Assuming it's an Enum to String mapping
    private List<CompetenceResponseDto> competences;  // Competence list for the response
}
