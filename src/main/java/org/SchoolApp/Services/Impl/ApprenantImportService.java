package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Enums.StatusEnum;
import org.SchoolApp.Datas.Repository.ApprenantRepository;
import org.SchoolApp.Datas.Repository.ReferentielRepository;
import org.SchoolApp.Services.Interfaces.EmailService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ApprenantImportService {

    @Autowired
    private ApprenantRepository apprenantRepository;

    @Autowired
    private ReferentielRepository referentielRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Importer des apprenants à partir d'un fichier Excel.
     *
     * @param file          Le fichier Excel à importer.
     * @param referentielId L'ID du référentiel auquel associer les apprenants.
     * @return Une liste d'apprenants dont l'importation a échoué.
     * @throws IOException En cas de problème lors de la lecture du fichier.
     */
    public List<ApprenantEntity> importApprenants(MultipartFile file, Long referentielId) throws IOException {
        List<ApprenantEntity> failedApprenants = new ArrayList<>();

        // Récupérer le référentiel
        ReferentielEntity referentiel = referentielRepository.findById(referentielId)
                .orElseThrow(() -> new RuntimeException("Référentiel non trouvé."));

        // Ouvrir le fichier Excel
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        // Parcourir les lignes du fichier Excel
        for (Row row : sheet) {
            try {
                ApprenantEntity apprenant = new ApprenantEntity();
                UserEntity user = new UserEntity();

                // Lecture des cellules du fichier Excel (colonnes: nom, prénom, email, téléphone)
                apprenant.setNomTuteur(row.getCell(0).getStringCellValue());
                apprenant.setPrenomTuteur(row.getCell(1).getStringCellValue());
                user.setEmail(row.getCell(2).getStringCellValue());
                user.setTelephone(row.getCell(3).getStringCellValue());

                // Générer un matricule unique
                apprenant.setMatricule(generateMatricule());

                // Associer l'utilisateur à l'apprenant
                apprenant.setUser(user);
                apprenant.setReferentiel(referentiel);

                // Vérifier l'unicité de l'apprenant (basée sur l'email)
                if (!isUnique(user)) {
                    failedApprenants.add(apprenant);  // Ajouter aux apprenants échoués
                    continue;
                }

                // Définir un mot de passe par défaut et le hacher
                String defaultPassword = generateDefaultPassword();
                user.setPassword(hashPassword(defaultPassword));
                user.setStatus(StatusEnum.INACTIF);

                // Sauvegarder l'apprenant et son utilisateur
                apprenantRepository.save(apprenant);

                // Envoyer l'email d'authentification avec le login (email) et le mot de passe
                emailService.sendAuthenticationEmail(user.getEmail(), user.getEmail(), defaultPassword);
            } catch (Exception e) {
                // Ajouter l'apprenant à la liste des apprenants échoués en cas d'erreur
                failedApprenants.add(new ApprenantEntity());  // Ajouter avec les infos disponibles
            }
        }

        // Fermer le fichier Excel
        workbook.close();

        return failedApprenants;
    }
    /**
     * Vérifie si un apprenant avec cet email est unique.
     *
     * @param user L'entité utilisateur à vérifier.
     * @return true si l'apprenant est unique, false sinon.
     */
    private boolean isUnique(UserEntity user) {
        return !apprenantRepository.existsByUser_Email(user.getEmail());
    }

    /**
     * Générer un matricule unique pour un apprenant.
     *
     * @return Le matricule généré.
     */
    private String generateMatricule() {
        return "MAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Générer un mot de passe par défaut pour l'apprenant.
     *
     * @return Le mot de passe généré.
     */
    private String generateDefaultPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Hacher le mot de passe de l'utilisateur (placeholder, peut être amélioré avec BCrypt).
     *
     * @param password Le mot de passe en clair.
     * @return Le mot de passe haché.
     */
    private String hashPassword(String password) {
        // Implémenter ici un véritable hachage de mot de passe avec BCrypt
        return password;
    }
}
