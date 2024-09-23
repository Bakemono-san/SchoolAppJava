package org.SchoolApp.Services.Impl;

import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Datas.Entity.ModulesEntity;
import org.SchoolApp.Datas.Entity.NotesEntity;
import org.SchoolApp.Datas.Enums.EtatEnum;
import org.SchoolApp.Datas.Repository.ApprenantRepository;
import org.SchoolApp.Datas.Repository.ModulesRepository;
import org.SchoolApp.Datas.Repository.NoteRepository;
import org.SchoolApp.Datas.Request.NoteRequest;
import org.SchoolApp.Datas.Request.NoteUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ApprenantRepository apprenantRepository;

    @Autowired
    private ModulesRepository modulesRepository;

    public List<NotesEntity> getAllNotes() {
        return noteRepository.findAll();
    }

    public NotesEntity addNote(Long apprenantId,float note,Long moduleId){
        NotesEntity noteEntity = new NotesEntity();
        ModulesEntity modules = modulesRepository.findById(moduleId).orElseThrow();
        ApprenantEntity apprenant = apprenantRepository.findById(apprenantId).orElseThrow();


        noteEntity.setNote(note);
        noteEntity.setModule(modules);
        noteEntity.setApprenant(apprenant);
        noteRepository.save(noteEntity);

        return noteEntity;
    }

    public List<NoteRequest> addNotesGroupe(List<NoteRequest> requests){
        for (NoteRequest noteRequest : requests) {
            addNote(noteRequest.getApprenantId(),noteRequest.getNote(),noteRequest.getModuleId());
        }

        return requests;
    }

    public List<NoteRequest> addNotesModules(List<NoteRequest> noteRequest){

        for (NoteRequest note: noteRequest) {
            ApprenantEntity apprenant = apprenantRepository.findById(note.getApprenantId()).orElseThrow();
            addNote(note.getApprenantId(),note.getNote(),note.getModuleId());
        }

        return noteRequest;
    }

    public List<NoteUpdate> updateNotes(List<NoteUpdate> notes) throws Exception {
        if(notes.stream().count() <= 0){
            throw new Exception("tu dois passer au moins une note a mettre a jour");
        }

        for (NoteUpdate note : notes) {
            NotesEntity OneNote = noteRepository.findById(note.getNoteId()).orElseThrow();
            OneNote.setNote(note.getNote());
            noteRepository.save(OneNote);
        }



        return notes;
    }

    public List<NotesEntity> findByReferentiel(Long ReferentielId){
        return noteRepository.findByApprenant_Referentiel_IdAndActivePromo(ReferentielId,EtatEnum.ACTIF);
    }
}
