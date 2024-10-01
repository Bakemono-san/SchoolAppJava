package org.SchoolApp.Datas.Seeders;

import jakarta.annotation.PostConstruct;
import org.SchoolApp.Datas.Entity.NotesEntity;
import org.SchoolApp.Datas.Entity.ModulesEntity;
import org.SchoolApp.Datas.Entity.ApprenantEntity;
import org.SchoolApp.Datas.Repository.NoteRepository;
import org.SchoolApp.Datas.Repository.ModulesRepository;
import org.SchoolApp.Datas.Repository.ApprenantRepository;
import org.springframework.stereotype.Component;

@Component
public class NotesSeeder {

    private final NoteRepository notesRepository;
    private final ModulesRepository modulesRepository;
    private final ApprenantRepository apprenantRepository;

    public NotesSeeder(NoteRepository notesRepository, ModulesRepository modulesRepository, ApprenantRepository apprenantRepository) {
        this.notesRepository = notesRepository;
        this.modulesRepository = modulesRepository;
        this.apprenantRepository = apprenantRepository;
    }

    @PostConstruct
    public void seedNotes() {
        if (notesRepository.count() == 0) {
            ModulesEntity module = modulesRepository.findById(1L).orElse(null);
            ApprenantEntity apprenant = apprenantRepository.findById(1L).orElse(null);
            if (module != null && apprenant != null) {
                NotesEntity note = new NotesEntity();
                note.setNote(10);
                note.setModule(module);
                note.setApprenant(apprenant);
                notesRepository.save(note);
            }
        }
    }
}
