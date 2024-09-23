package org.SchoolApp.Web.Controller.Impl;

import org.SchoolApp.Datas.Entity.NotesEntity;
import org.SchoolApp.Datas.Request.NoteRequest;
import org.SchoolApp.Datas.Request.NoteUpdate;
import org.SchoolApp.Services.Impl.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;

    @PostMapping("/apprenants")
    public List<NoteRequest> addNoteModules(@RequestBody List<NoteRequest> noteRequest){
        return noteService.addNotesModules(noteRequest);
    }

    @PostMapping("modules/{id}")
    public List<NoteRequest> addNotesGroupe(@RequestBody List<NoteRequest> noteRequest){
        return noteService.addNotesGroupe(noteRequest);
    }

    @PatchMapping("apprenants/{id}")
    public List<NoteUpdate> updateNoteModules(@RequestBody List<NoteUpdate> noteUpdate) throws Exception {
        return noteService.updateNotes(noteUpdate);
    }

    @GetMapping("")
    public List<NotesEntity> getAllNotes(){
        return  noteService.getAllNotes();
    }

    @GetMapping("referentiels/{id}")
    public List<NotesEntity> findByReferentiel(@RequestParam Long referentielId){
        return noteService.findByReferentiel(referentielId);
    }


}
