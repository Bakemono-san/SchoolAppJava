package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Datas.Entity.ReferentielEntity;
import org.SchoolApp.Datas.Entity.UserEntity;
import org.SchoolApp.Datas.Enums.StatusEnum;
import org.SchoolApp.Datas.Repository.ApprenantRepository;
import org.SchoolApp.Datas.Repository.ReferentielRepository;
import org.SchoolApp.Datas.Repository.UserRepository;
import org.SchoolApp.Services.Interfaces.ApprenantService;
import org.SchoolApp.Services.Interfaces.EmailService;
import org.SchoolApp.Services.Interfaces.QRCodeService;
import org.SchoolApp.Web.Validators.ApprenantValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ApprenantServiceImpl implements ApprenantService {

    @Autowired
    private ApprenantRepository apprenantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReferentielRepository referentielRepository;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApprenantValidator apprenantValidator;

    @Override
    public ApprenantEntity createApprenant(ApprenantEntity apprenant) {
        apprenantValidator.validateApprenant(apprenant);

        UserEntity user = apprenant.getUser();

        String defaultPassword = generateDefaultPassword();
        user.setPassword(hashPassword(defaultPassword));

        if (user.getStatus() == null) {
            user.setStatus(StatusEnum.ACTIF);
        }

        userRepository.save(user);
        apprenant.setUser(user);

        ReferentielEntity referentiel = referentielRepository.findById(apprenant.getReferentiel().getId())
                .orElseThrow(() -> new RuntimeException("Référentiel non trouvé."));
        apprenant.setReferentiel(referentiel);

        String matricule = generateMatricule();
        apprenant.setMatricule(matricule);

        String qrCodeLink = qrCodeService.generateQRCode(matricule);
        apprenant.setQrCodeLink(qrCodeLink);

        ApprenantEntity savedApprenant = apprenantRepository.save(apprenant);

        emailService.sendAuthenticationEmail(user.getEmail(), user.getEmail(), defaultPassword);

        return savedApprenant;
    }

    @Override
    public List<ApprenantEntity> getAllApprenants() {
        return apprenantRepository.findAll();
    }

    @Override
    public ApprenantEntity getApprenantById(Long id) {
        return apprenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apprenant non trouvé avec ID: " + id));
    }

    @Override
    public ApprenantEntity updateApprenant(Long id, ApprenantEntity apprenantUpdates) {
        ApprenantEntity existingApprenant = apprenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apprenant non trouvé avec ID: " + id));

        if (apprenantUpdates.getNomTuteur() != null) {
            existingApprenant.setNomTuteur(apprenantUpdates.getNomTuteur());
        }
        if (apprenantUpdates.getPrenomTuteur() != null) {
            existingApprenant.setPrenomTuteur(apprenantUpdates.getPrenomTuteur());
        }

        return apprenantRepository.save(existingApprenant);
    }

    @Override
    public void deleteApprenant(Long id) {
        ApprenantEntity apprenant = apprenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Apprenant non trouvé avec ID: " + id));
        apprenantRepository.softDelete(id);
    }

    private String generateMatricule() {
        return "MAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateDefaultPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private String hashPassword(String password) {
        // Remplacer par le hachage réel avec BCrypt ou autre
        return password;
    }
}
