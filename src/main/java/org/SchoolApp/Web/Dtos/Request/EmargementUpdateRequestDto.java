package org.SchoolApp.Web.Dtos.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;


@Data
public class EmargementUpdateRequestDto {

    private LocalTime entree;
    private LocalTime sortie;

}
