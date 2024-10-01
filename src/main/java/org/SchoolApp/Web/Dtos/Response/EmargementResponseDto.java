package org.SchoolApp.Web.Dtos.Response;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmargementResponseDto {
    private Long id;
    private LocalDate date;
    private LocalTime entree;
    private LocalTime sortie;
    private Long userId;

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getEntree() {
        return entree;
    }

    public void setEntree(LocalTime entree) {
        this.entree = entree;
    }

    public LocalTime getSortie() {
        return sortie;
    }

    public void setSortie(LocalTime sortie) {
        this.sortie = sortie;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
