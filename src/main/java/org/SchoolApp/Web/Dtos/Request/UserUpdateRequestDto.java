package org.SchoolApp.Web.Dtos.Request;

import lombok.Data;
import jakarta.validation.constraints.Email;

@Data
public class UserUpdateRequestDto {
    private String nom;
    private String prenom;
    private String telephone;

    @Email(message = "Email non valide")
    private String email;
    private String photo;
    private String adresse;

    // Le mot de passe est optionnel pour la mise à jour
    private String password;
}
