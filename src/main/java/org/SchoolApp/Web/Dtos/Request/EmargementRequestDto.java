package org.SchoolApp.Web.Dtos.Request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;


@Data
public class EmargementRequestDto {


    @NotNull(message = "L'ID de l'apprenant est obligatoire.")
    private Long apprenantId;

    private LocalDate date;

    private LocalTime entree;

    private LocalTime sortie;
}
