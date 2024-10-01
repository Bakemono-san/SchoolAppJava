package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Datas.Entity.ModulesEntity;
import org.SchoolApp.Datas.Entity.NotesEntity;
import org.SchoolApp.Datas.Enums.EtatEnum;
import org.SchoolApp.Datas.Repository.ApprenantRepository;
import org.SchoolApp.Datas.Repository.ModulesRepository;
import org.SchoolApp.Datas.Repository.NoteRepository;
import org.SchoolApp.Web.Dtos.Request.NoteRequest;
import org.SchoolApp.Web.Dtos.Request.NoteUpdate;
import org.odc.core.Exceptions.ArgumentInsuffisantException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ApprenantRepository apprenantRepository;

    @Autowired
    private ModulesRepository modulesRepository;

    // Méthode pour ajouter des notes à un groupe d'apprenants pour un module donné
    public List<NotesEntity> addNotesGroupe(List<NoteRequest> requests, Long moduleId) {
        // Récupérer le module
        ModulesEntity module = modulesRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module non trouvé"));

        // Parcourir chaque requête de note
        for (NoteRequest noteRequest : requests) {
            // Vérifier que l’apprenant appartient à la promotion active
            ApprenantEntity apprenant = apprenantRepository.findById(noteRequest.getApprenant())
                    .orElseThrow(() -> new RuntimeException("Apprenant non trouvé"));

            // Vérification que l'apprenant appartient à la promotion active
            if (!isApprenantInActivePromo(apprenant)) {
                throw new IllegalArgumentException("Apprenant n'appartient pas à la promotion active");
            }

            // Créer la note
            NotesEntity noteEntity = new NotesEntity();
            noteEntity.setNote(noteRequest.getNote());
            noteEntity.setModule(module);
            noteEntity.setApprenant(apprenant);

            // Sauvegarder la note
            noteRepository.save(noteEntity);
        }

        return findAll();  // Retourne toutes les notes après ajout
    }

    // Méthode pour ajouter des notes à un apprenant pour plusieurs modules
    public NotesEntity addNotesToApprenant(NoteRequest noteRequest) {
        // Récupérer l'apprenant via son ID
        ApprenantEntity apprenant = apprenantRepository.findById(noteRequest.getApprenant())
                .orElseThrow(() -> new RuntimeException("Apprenant non trouvé"));

        // Vérifier si l'apprenant appartient à une promotion active
        if (!isApprenantInActivePromo(apprenant)) {
            throw new IllegalArgumentException("L'apprenant n'appartient pas à la promotion active");
        }

        // Récupérer le module via son ID
        ModulesEntity module = modulesRepository.findById(noteRequest.getModule())
                .orElseThrow(() -> new RuntimeException("Module non trouvé"));

        // Créer la note pour cet apprenant et ce module
        NotesEntity noteEntity = new NotesEntity();
        noteEntity.setNote(noteRequest.getNote());
        noteEntity.setApprenant(apprenant);
        noteEntity.setModule(module);

        // Sauvegarder la note et la retourner
        return noteRepository.save(noteEntity);
    }

    // Méthode pour récupérer les notes des apprenants d'un référentiel dans la promotion active
    public List<NotesEntity> findByReferentiel(Long referentielId) {
        return noteRepository.findByApprenant_Referentiel_IdAndActivePromo(referentielId, EtatEnum.ACTIF);
    }

    // Vérification que l'apprenant est dans une promotion active
    public boolean isApprenantInActivePromo(ApprenantEntity apprenant) {
        return apprenant.getPromo().getEtat().equals(EtatEnum.ACTIF);
    }

    // Méthode pour récupérer toutes les notes
    public List<NotesEntity> findAll() {
        return noteRepository.findAll();
    }

    // Méthode pour mettre à jour une note
    public NotesEntity updateNote(Long noteId, NoteUpdate noteUpdate) {
        NotesEntity noteEntity = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note non trouvée"));
        noteEntity.setNote(noteUpdate.getNote());
        return noteRepository.save(noteEntity);
    }

    // Méthode pour supprimer une note
    public Optional<NotesEntity> deleteNoteById(Long noteId) {
        NotesEntity noteEntity = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note non trouvée"));
        noteRepository.delete(noteEntity);
        return Optional.of(noteEntity);
    }

    public List<NoteUpdate> updateNotes(List<NoteUpdate> noteUpdates, Long apprenantId) {
        // Parcourir les mises à jour de notes dans la liste
        for (NoteUpdate noteUpdate : noteUpdates) {
            NotesEntity noteEntity = noteRepository.findById(noteUpdate.getNoteId())
                    .orElseThrow(() -> new RuntimeException("Note non trouvée"));

            // Vérifier que la note appartient à l'apprenant spécifié
            if (!noteEntity.getApprenant().getId().equals(apprenantId)) {
                throw new IllegalArgumentException("La note n'appartient pas à cet apprenant");
            }

            // Mettre à jour la valeur de la note
            noteEntity.setNote(noteUpdate.getNote());
            noteRepository.save(noteEntity);
        }

        // Retourner la liste des notes mises à jour
        return noteUpdates;
    }

}
