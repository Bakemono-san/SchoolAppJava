package org.SchoolApp.Web.Controller.Impl;

import org.SchoolApp.Datas.Entity.NotesEntity;
import org.SchoolApp.Web.Controller.Interfaces.CrudController;
import org.SchoolApp.Web.Dtos.Mapper.NoteRequestMapper;
import org.SchoolApp.Web.Dtos.Request.NoteRequest;
import org.SchoolApp.Web.Dtos.Request.NoteUpdate;
import org.SchoolApp.Services.Impl.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping("/modules/{id}")
    public ResponseEntity<List<NotesEntity>> addNotesGroupe(@RequestBody List<NoteRequest> noteRequests, @PathVariable Long id) {
        try {
            List<NotesEntity> notes = noteService.addNotesGroupe(noteRequests, id);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/apprenants")
    public ResponseEntity<List<NotesEntity>> addNoteModules(@RequestBody NoteRequest noteRequest) {
        try {
            NotesEntity note = noteService.addNotesToApprenant(noteRequest);
            return ResponseEntity.ok(List.of(note)); // Retourne une liste contenant la note ajoutée
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PatchMapping("/apprenants/{id}")
    public ResponseEntity<List<NoteUpdate>> updateNoteModules(@RequestBody List<NoteUpdate> noteUpdates, @PathVariable Long id) {
        try {
            // Mise à jour des notes pour un apprenant spécifique
            List<NoteUpdate> updatedNotes = noteService.updateNotes(noteUpdates, id);
            return ResponseEntity.ok(updatedNotes);
        } catch (RuntimeException e) {
            // Si l'apprenant ou la note n'existe pas, on retourne un 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // Gestion générique des erreurs
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @GetMapping("/referentiels/{id}")
    public ResponseEntity<List<NotesEntity>> findByReferentiel(@PathVariable Long id) {
        try {
            List<NotesEntity> notes = noteService.findByReferentiel(id);
            return ResponseEntity.ok(notes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/export/referentiels/{id}")
    public ResponseEntity<byte[]> exportNotesByReferentiel(@PathVariable Long id) {
        // Implémentation pour générer le relevé de notes
        // Exportation sous format PDF ou Excel
        return null;  // Placeholder pour l'export
    }

    @GetMapping("/export/apprenants/{id}")
    public ResponseEntity<byte[]> exportNotesByApprenant(@PathVariable Long id) {
        // Implémentation pour générer le relevé de notes pour un apprenant
        return null;  // Placeholder pour l'export
    }
}
