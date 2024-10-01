package org.SchoolApp.Web.Dtos.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.SchoolApp.Datas.Enums.StatusEnum;

@Data
public class UserRequestDto {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    private String adresse;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password; // Assurez-vous que ce champ est sécurisé lors de son utilisation.

    @NotNull(message = "Le rôle est obligatoire")
    private Long roleId; // Id du rôle de l'utilisateur

    private Long fonctionId; // Id de la fonction de l'utilisateur

    @NotNull(message = "Le statut est obligatoire")
    private StatusEnum status; // Changement de String à StatusEnum

    private String photo; // Si vous avez un champ photo dans UserEntity
}
