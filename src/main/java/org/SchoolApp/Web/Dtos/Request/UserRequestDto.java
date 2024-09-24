package org.SchoolApp.Web.Dtos.Request;

import lombok.Data;
import org.SchoolApp.Datas.Enums.StatusEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class UserRequestDto {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @Email(message = "Email non valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @NotBlank(message ="La photo est obligatoire")
    private String photo;

    @NotNull(message = "L'ID du rôle est obligatoire")
    private Long roleId;

    private Long fonction_id;

    private StatusEnum status = StatusEnum.ACTIF; // Valeur par défaut ACTIF
}
