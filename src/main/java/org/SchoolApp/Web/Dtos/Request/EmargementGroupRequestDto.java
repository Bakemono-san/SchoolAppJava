package org.SchoolApp.Web.Dtos.Request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;


public class EmargementGroupRequestDto {

    @NotNull(message = "La date est obligatoire.")
    private LocalDate date;

    @NotEmpty(message = "La liste des apprenants est obligatoire.")
    private List<Long> apprenantIds;

    // Getters et Setters
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Long> getApprenantIds() {
        return apprenantIds;
    }

    public void setApprenantIds(List<Long> apprenantIds) {
        this.apprenantIds = apprenantIds;
    }
}
